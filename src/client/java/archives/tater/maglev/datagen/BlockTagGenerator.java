package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(MaglevBlocks.MAGLEV_RAILS).add(MaglevBlocks.MAGLEV_RAIL.stream());
        valueLookupBuilder(MaglevBlocks.POWERED_MAGLEV_RAILS).add(MaglevBlocks.POWERED_MAGLEV_RAIL.stream());
        valueLookupBuilder(MaglevBlocks.VARIABLE_MAGLEV_RAILS).add(MaglevBlocks.VARIABLE_MAGLEV_RAIL.stream());
        valueLookupBuilder(MaglevBlocks.HOVERABLE_RAILS)
                .forceAddTag(MaglevBlocks.MAGLEV_RAILS)
                .forceAddTag(MaglevBlocks.POWERED_MAGLEV_RAILS)
                .forceAddTag(MaglevBlocks.VARIABLE_MAGLEV_RAILS);
        valueLookupBuilder(BlockTags.RAILS)
                .forceAddTag(MaglevBlocks.HOVERABLE_RAILS);
    }
}
