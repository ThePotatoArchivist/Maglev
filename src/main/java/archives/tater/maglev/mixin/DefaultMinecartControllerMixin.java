package archives.tater.maglev.mixin;

import archives.tater.maglev.block.OxidizablePoweredRailBlock;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevDataAttachments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static archives.tater.maglev.CopperBlockSetUtil.isOf;
import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(OldMinecartBehavior.class)
public abstract class DefaultMinecartControllerMixin extends MinecartBehavior {
    protected DefaultMinecartControllerMixin(AbstractMinecart minecart) {
        super(minecart);
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
        if (!minecart.hasAttached(HOVER_HEIGHT)) return y;
        return y - minecart.getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @ModifyExpressionValue(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getY()I")
    )
    private int addHeight(int original) {
        return original + minecart.getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @WrapOperation(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean checkPowered(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || isOf(instance.getBlock(), MaglevBlocks.POWERED_MAGLEV_RAIL);
    }

    @ModifyExpressionValue(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private BlockState updateSpeed(BlockState original) {
        OxidizablePoweredRailBlock.updateSpeed(minecart, original);
        return original;
    }

    @ModifyExpressionValue(
            method = "moveAlongTrack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private BlockState updateOnVariableRail(BlockState original, @Local BlockPos pos) {
        if (!isOf(original.getBlock(), MaglevBlocks.VARIABLE_MAGLEV_RAIL)) return original;

        minecart.setAttached(HOVER_HEIGHT, minecart.level().getBestNeighborSignal(pos));

        return original;
    }

    @ModifyExpressionValue(
            method = "getKnownMovement",
            at = @At(value = "CONSTANT", args = {
                    "doubleValue=-0.4",
                    "doubleValue=0.4"
            })
    )
    private double changeMaxSpeed(double original) {
        return minecart.getAttachedOrElse(MaglevDataAttachments.SPEED_MULTIPLIER, 1.0) * original;
    }

    @ModifyReturnValue(
            method = "getMaxSpeed",
            at = @At("RETURN")
    )
    private double changeMaxSpeed2(double original) {
        return minecart.getAttachedOrElse(MaglevDataAttachments.SPEED_MULTIPLIER, 1.0) * original;
    }
}