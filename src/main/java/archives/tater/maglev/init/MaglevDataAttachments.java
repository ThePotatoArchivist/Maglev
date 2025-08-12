package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;

@SuppressWarnings("UnstableApiUsage")
public class MaglevDataAttachments {
    public static AttachmentType<Integer> HOVER_HEIGHT = AttachmentRegistry.create(Maglev.id("hover_height"), builder -> builder
            .persistent(Codec.INT)
            .syncWith(PacketCodecs.INTEGER, AttachmentSyncPredicate.all())
    );

    public static void init() {

    }
}
