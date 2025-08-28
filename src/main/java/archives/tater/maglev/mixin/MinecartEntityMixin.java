package archives.tater.maglev.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static archives.tater.maglev.init.MaglevDataAttachments.EMPTY_TIME;
import static archives.tater.maglev.init.MaglevDataAttachments.MAX_EMPTY_TIME;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MinecartEntity.class)
public abstract class MinecartEntityMixin extends AbstractMinecartEntity {
    protected MinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void tickAge(CallbackInfo ci) {
        if (getWorld().isClient || !hasAttached(MAX_EMPTY_TIME)) return;
        if (hasPassengers()) {
            removeAttached(EMPTY_TIME);
            return;
        }
        @SuppressWarnings("DataFlowIssue")
        int maxEmptyAge = getAttached(MAX_EMPTY_TIME);
        int emptyAge = getAttachedOrElse(EMPTY_TIME, 0);
        if (emptyAge > maxEmptyAge) {
            discard();
            return;
        }
        setAttached(EMPTY_TIME, emptyAge + 1);
    }
}
