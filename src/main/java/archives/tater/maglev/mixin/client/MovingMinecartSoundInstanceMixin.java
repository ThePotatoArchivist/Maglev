package archives.tater.maglev.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_HEIGHT;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MinecartSoundInstance.class)
public class MovingMinecartSoundInstanceMixin {
    @Shadow @Final private AbstractMinecart minecart;

    @ModifyVariable(
            method = "tick",
            at = @At(value = "STORE", ordinal = 0)
    )
    private boolean cancelSound(boolean original) {
        return original || minecart.hasAttached(HOVER_HEIGHT);
    }
}
