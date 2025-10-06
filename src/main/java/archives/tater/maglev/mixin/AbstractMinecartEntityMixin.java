package archives.tater.maglev.mixin;

import archives.tater.maglev.init.MaglevBlocks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;
import static archives.tater.maglev.init.MaglevDataAttachments.SPEED_MULTIPLIER;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity {
    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private boolean checkRail(int x, int y, int z) {
        var state = getEntityWorld().getBlockState(new BlockPos(x, y, z));
        if (!state.isIn(MaglevBlocks.HOVERABLE_RAILS)) return false;
        return true;
    }

    @ModifyVariable(
            method = "getRailOrMinecartPos",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;getEntityWorld()Lnet/minecraft/world/World;", ordinal = 0),
            ordinal = 1
    )
    private int addHoverHeight(int y, @Local(ordinal = 0) int x, @Local(ordinal = 2) int z) {
        var world = getEntityWorld();
        if (hasAttached(HOVER_HEIGHT)) {
            var movedY = y - getAttachedOrElse(HOVER_HEIGHT, 0);
            if (checkRail(x, movedY, z) || checkRail(x, movedY - 1, z))
                return movedY;
        }

        var blockPos = getBlockPos().mutableCopy();
        for (int i = 0; i <= 15; i++) { // TODO unhardcode? or at least make constant
            var state = world.getBlockState(blockPos);
            if (state.isIn(MaglevBlocks.HOVERABLE_RAILS)) {
                if (i <= 1) return y;
                setAttached(HOVER_HEIGHT, i);
                return blockPos.getY();
            }
            blockPos.move(Direction.DOWN);
        }

        removeAttached(HOVER_HEIGHT);
        return y;
    }

    @ModifyExpressionValue(
            method = "applySlowdown",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/MinecartController;getSpeedRetention()D")
    )
    private double frictionless(double original) {
        return hasAttached(SPEED_MULTIPLIER) ? 1 : original;
    }

    @Inject(
            method = "moveOffRail",
            at = @At("HEAD")
    )
    private void removeHoverSpeed(ServerWorld world, CallbackInfo ci) {
        removeAttached(SPEED_MULTIPLIER);
    }
}
