package archives.tater.maglev.mixin;

import archives.tater.maglev.block.OxidizablePoweredRailBlock;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevDataAttachments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractMinecartEntity.class)
public abstract class DefaultMinecartControllerMixin extends VehicleEntity {

    public DefaultMinecartControllerMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = {
                    "snapPositionToRail",
                    "snapPositionToRailWithOffset"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;<init>(III)V"),
            index = 1
    )
    private int snapToHover(int y) {
        if (!hasAttached(HOVER_HEIGHT)) return y;
        return y - getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @ModifyExpressionValue(
            method = "moveOnRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;getY()I")
    )
    private int addHeight(int original) {
        return original + getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @WrapOperation(
            method = "moveOnRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z")
    )
    private boolean checkPowered(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || MaglevBlocks.POWERED_MAGLEV_RAIL.contains(instance.getBlock());
    }

    @Inject(
            method = "moveOnRail",
            at = @At("HEAD")
    )
    private void updateSpeed(BlockPos pos, BlockState state, CallbackInfo ci) {
        OxidizablePoweredRailBlock.updateSpeed((AbstractMinecartEntity) (Object) this, state);
    }

    @Inject(
            method = "moveOnRail",
            at = @At("HEAD")
    )
    private void updateOnVariableRail(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!MaglevBlocks.VARIABLE_MAGLEV_RAIL.contains(state.getBlock())) return;

        setAttached(HOVER_HEIGHT, getWorld().getReceivedRedstonePower(pos));
    }

//    @ModifyExpressionValue(
//            method = "limitSpeed",
//            at = @At(value = "CONSTANT", args = {
//                    "doubleValue=-0.4",
//                    "doubleValue=0.4"
//            })
//    )
//    private double changeMaxSpeed(double original) {
//        return getAttachedOrElse(MaglevDataAttachments.SPEED_MULTIPLIER, 1.0) * original;
//    }

    @ModifyReturnValue(
            method = "getMaxSpeed",
            at = @At("RETURN")
    )
    private double changeMaxSpeed2(double original) {
        return getAttachedOrElse(MaglevDataAttachments.SPEED_MULTIPLIER, 1.0) * original;
    }
}