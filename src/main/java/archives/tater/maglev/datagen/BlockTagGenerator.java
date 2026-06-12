package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlockItemIds;
import archives.tater.maglev.init.MaglevBlockItemTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.BlockItemTagAppender;
import net.minecraft.references.BlockItemId;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private BlockItemTagAppender<Block> builder(BlockItemTagId tag) {
        return builder(tag.block());
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(MaglevBlockItemTags.MAGLEV_RAILS).addAll(MaglevBlockItemIds.MAGLEV_RAIL.map(BlockItemId::block));
        builder(MaglevBlockItemTags.POWERED_MAGLEV_RAILS).addAll(MaglevBlockItemIds.POWERED_MAGLEV_RAIL.map(BlockItemId::block));
        builder(MaglevBlockItemTags.VARIABLE_MAGLEV_RAILS).addAll(MaglevBlockItemIds.VARIABLE_MAGLEV_RAIL.map(BlockItemId::block));
        builder(MaglevBlockItemTags.HOVERABLE_RAILS)
                .addTag(MaglevBlockItemTags.MAGLEV_RAILS.block())
                .addTag(MaglevBlockItemTags.POWERED_MAGLEV_RAILS.block())
                .addTag(MaglevBlockItemTags.VARIABLE_MAGLEV_RAILS.block());
        builder(BlockTags.RAILS)
                .addTag(MaglevBlockItemTags.HOVERABLE_RAILS.block());
        builder(MaglevBlockItemTags.MANUALLY_OXIDIZABLE)
                .addTag(MaglevBlockItemTags.HOVERABLE_RAILS.block());
    }
}
