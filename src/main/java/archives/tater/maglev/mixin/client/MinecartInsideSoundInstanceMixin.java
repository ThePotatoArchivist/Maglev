package archives.tater.maglev.mixin.client;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(RidingMinecartSoundInstance.class)
public class MinecartInsideSoundInstanceMixin {
    @Shadow @Final private AbstractMinecart minecart;

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;isRemoved()Z")
    )
    private boolean cancelSound(boolean original) {
        return original || ((AttachmentTarget) minecart).hasAttached(HOVER_HEIGHT);
    }
}
