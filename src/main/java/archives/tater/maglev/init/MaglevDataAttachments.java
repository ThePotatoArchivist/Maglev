package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;

public class MaglevDataAttachments {
    public static final AttachmentType<Integer> HOVER_HEIGHT = AttachmentRegistry.create(Maglev.id("hover_height"), builder -> builder
            .persistent(Codec.INT)
            .syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );

    public static final AttachmentType<Double> SPEED_MULTIPLIER = AttachmentRegistry.create(Maglev.id("speed_multiplier"), builder -> builder
            .persistent(Codec.DOUBLE)
            .syncWith(ByteBufCodecs.DOUBLE, AttachmentSyncPredicate.all())
    );

    public static void init() {

    }
}
