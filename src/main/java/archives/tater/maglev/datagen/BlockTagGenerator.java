package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(MaglevBlocks.MAGLEV_RAILS).addAll(MaglevBlocks.MAGLEV_RAIL.asList());
        valueLookupBuilder(MaglevBlocks.POWERED_MAGLEV_RAILS).addAll(MaglevBlocks.POWERED_MAGLEV_RAIL.asList());
        valueLookupBuilder(MaglevBlocks.VARIABLE_MAGLEV_RAILS).addAll(MaglevBlocks.VARIABLE_MAGLEV_RAIL.asList());
        valueLookupBuilder(MaglevBlocks.HOVERABLE_RAILS)
                .forceAddTag(MaglevBlocks.MAGLEV_RAILS)
                .forceAddTag(MaglevBlocks.POWERED_MAGLEV_RAILS)
                .forceAddTag(MaglevBlocks.VARIABLE_MAGLEV_RAILS);
        valueLookupBuilder(BlockTags.RAILS)
                .forceAddTag(MaglevBlocks.HOVERABLE_RAILS);
        valueLookupBuilder(MaglevBlocks.MANUALLY_OXIDIZABLE)
                .forceAddTag(MaglevBlocks.HOVERABLE_RAILS);
    }
}
