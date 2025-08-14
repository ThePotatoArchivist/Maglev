package archives.tater.maglev.mixin;

import archives.tater.maglev.OxidizablePoweredRailBlock;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevDataAttachments;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;
import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_SPEED;
import static java.lang.Math.abs;
import static java.lang.Math.copySign;

@SuppressWarnings("UnstableApiUsage")
@Mixin(DefaultMinecartController.class)
@Debug(export = true)
public abstract class DefaultMinecartControllerMixin extends MinecartController {
    protected DefaultMinecartControllerMixin(AbstractMinecartEntity minecart) {
        super(minecart);
    }

    @ModifyArg(
            method = {
                    "snapPositionToRail",
                    "simulateMovement"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;<init>(III)V"),
            index = 1
    )
    private int snapToHover(int y) {
        if (!minecart.hasAttached(HOVER_HEIGHT)) return y;
        return y - minecart.getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @ModifyExpressionValue(
            method = "moveOnRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;getY()I")
    )
    private int addHeight(int original) {
        return original + minecart.getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @WrapOperation(
            method = "moveOnRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z")
    )
    private boolean checkPowered(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || MaglevBlocks.POWERED_MAGLEV_RAIL.contains(instance.getBlock());
    }

    @ModifyExpressionValue(
            method = "moveOnRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;")
    )
    private BlockState updateSpeed(BlockState original) {
        OxidizablePoweredRailBlock.updateSpeed(minecart, original);
        return original;
    }

    @ModifyExpressionValue(
            method = {
                    "limitSpeed",
                    "getMaxSpeed"
            },
            at = {
                    @At(value = "CONSTANT", args = "doubleValue=-0.4"),
                    @At(value = "CONSTANT", args = "doubleValue=0.4")
            }
    )
    private double increaseMaxSpeed(double original) {
        return minecart.hasAttached(HOVER_SPEED) ? copySign(minecart.getAttachedOrElse(MaglevDataAttachments.HOVER_SPEED, abs(original)), original) : original;
    }
}