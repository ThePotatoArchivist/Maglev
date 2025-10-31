package archives.tater.maglev.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.sound.MinecartInsideSoundInstance;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MinecartInsideSoundInstance.class)
public class MinecartInsideSoundInstanceMixin {
    @Shadow @Final private AbstractMinecartEntity minecart;

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;isRemoved()Z")
    )
    private boolean cancelSound(boolean original) {
        return original || minecart.hasAttached(HOVER_HEIGHT);
    }
}
