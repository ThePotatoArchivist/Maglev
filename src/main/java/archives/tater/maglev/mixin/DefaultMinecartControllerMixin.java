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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractMinecart.class)
public abstract class DefaultMinecartControllerMixin extends VehicleEntity {

    public DefaultMinecartControllerMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = {
                    "getPos",
                    "getPosOffs"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;<init>(III)V"),
            index = 1
    )
    private int snapToHover(int y) {
        if (!hasAttached(HOVER_HEIGHT)) return y;
        return y - getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @ModifyExpressionValue(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I")
    )
    private int addHeight(int original) {
        return original + getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @WrapOperation(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean checkPowered(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || MaglevBlocks.POWERED_MAGLEV_RAIL.contains(instance.getBlock());
    }

    @Inject(
            method = "moveAlongTrack",
            at = @At("HEAD")
    )
    private void updateSpeed(BlockPos pos, BlockState state, CallbackInfo ci) {
        OxidizablePoweredRailBlock.updateSpeed((AbstractMinecart) (Object) this, state);
    }

    @Inject(
            method = "moveAlongTrack",
            at = @At("HEAD")
    )
    private void updateOnVariableRail(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!MaglevBlocks.VARIABLE_MAGLEV_RAIL.contains(state.getBlock())) return;

        setAttached(HOVER_HEIGHT, level().getBestNeighborSignal(pos));
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