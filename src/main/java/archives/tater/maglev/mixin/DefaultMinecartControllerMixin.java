package archives.tater.maglev.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(DefaultMinecartController.class)
@Debug(export = true)
public abstract class DefaultMinecartControllerMixin extends MinecartController {
    protected DefaultMinecartControllerMixin(AbstractMinecartEntity minecart) {
        super(minecart);
    }

    @ModifyArg(
            method = "snapPositionToRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;<init>(III)V", ordinal = 1),
            index = 1
    )
    private int snapToHover(int y) {
        if (!minecart.hasAttached(HOVER_HEIGHT)) return y;
        return y - minecart.getAttachedOrElse(HOVER_HEIGHT, 0);
    }

    @ModifyArg(
            method = "simulateMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;<init>(III)V", ordinal = 1),
            index = 1
    )
    private int moveHover(int y) {
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
}