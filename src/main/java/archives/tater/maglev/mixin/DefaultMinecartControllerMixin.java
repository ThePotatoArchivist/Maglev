package archives.tater.maglev.mixin;

import archives.tater.maglev.init.MaglevDataAttachments;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
        if (!minecart.hasAttached(MaglevDataAttachments.HOVER_HEIGHT)) return y;
        return y - minecart.getAttachedOrElse(MaglevDataAttachments.HOVER_HEIGHT, 0);
    }

    @ModifyArg(
            method = "simulateMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;<init>(III)V", ordinal = 1),
            index = 1
    )
    private int moveHover(int y) {
        if (!minecart.hasAttached(MaglevDataAttachments.HOVER_HEIGHT)) return y;
        return y - minecart.getAttachedOrElse(MaglevDataAttachments.HOVER_HEIGHT, 0);
    }

    @ModifyExpressionValue(
            method = "moveOnRail",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;getY()I")
    )
    private int addHeight(int original) {
        return original + minecart.getAttachedOrElse(MaglevDataAttachments.HOVER_HEIGHT, 0);
    }
}