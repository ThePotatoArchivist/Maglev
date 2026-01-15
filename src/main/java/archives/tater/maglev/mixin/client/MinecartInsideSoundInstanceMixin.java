package archives.tater.maglev.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(RidingMinecartSoundInstance.class)
public class MinecartInsideSoundInstanceMixin {
    @Shadow @Final private AbstractMinecart minecart;

    @ModifyReturnValue(
            method = "shoudlPlaySound",
            at = @At("RETURN")
    )
    private boolean cancelSound(boolean original) {
        return original && !minecart.hasAttached(HOVER_HEIGHT);
    }
}
