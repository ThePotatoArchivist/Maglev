package archives.tater.maglev.mixin;

import archives.tater.maglev.init.MaglevBlocks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@Debug(export = true)
@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity {
    @Shadow public abstract BlockPos getRailOrMinecartPos();

    @Shadow protected abstract float getVelocityMultiplier();

    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @WrapOperation(
            method = "getRailOrMinecartPos",
            at = @At(value = "NEW", target = "(III)Lnet/minecraft/util/math/BlockPos;")
    )
    private BlockPos addHoverHeight(int x, int y, int z, Operation<BlockPos> original) {
        if (!hasAttached(HOVER_HEIGHT)) return original.call(x, y, z);
        var hoverHeight = getAttachedOrElse(HOVER_HEIGHT, 0);
        var altered = new BlockPos(x, y - hoverHeight + 1, z);
        if (getWorld().getBlockState(altered).isIn(MaglevBlocks.MAGLEV_RAILS))
            return altered;
        removeAttached(HOVER_HEIGHT);
        return original.call(x, y, z);
    }

    @ModifyExpressionValue(
            method = "getRailOrMinecartPos",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
    )
    private boolean setHoverHeight(boolean original, @Cancellable CallbackInfoReturnable<BlockPos> cir) {
        if (original || hasAttached(HOVER_HEIGHT)) return true;
        // essentially adding an else clause
        var world = getWorld();
        var blockPos = getBlockPos().mutableCopy();

        for (int i = 1; i <= 15; i++) {
            blockPos.move(Direction.DOWN);
            if (world.getBlockState(blockPos).isIn(MaglevBlocks.MAGLEV_RAILS)) {
                setAttached(HOVER_HEIGHT, i);
                cir.setReturnValue(blockPos.toImmutable());
                return false;
            }
        }

        return false;
    }

    @ModifyExpressionValue(
            method = "applySlowdown",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/MinecartController;getSpeedRetention()D")
    )
    private double frictionless(double original) {
        return hasAttached(HOVER_HEIGHT) ? 1 : original;
    }

//    @Inject(
//            method = "applyGravity",
//            at = @At("HEAD")
//    )
//    private void hover(CallbackInfo ci) {
//        if (hasAttached(MaglevDataAttachments.HOVER_HEIGHT) && getRailOrMinecartPos().getY() > getY()) {
//            var velocity = getVelocity();
//            setVelocity(velocity.x, Math.clamp(0.1 - velocity.y, 0, 0.1), velocity.z);
//        }
//    }
}
