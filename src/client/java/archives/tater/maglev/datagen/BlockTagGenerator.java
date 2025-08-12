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
        valueLookupBuilder(MaglevBlocks.MAGLEV_RAILS)
                .add(MaglevBlocks.MAGLEV_RAIL.stream())
                .add(MaglevBlocks.VARIABLE_MAGLEV_RAIL.stream())
                .add(MaglevBlocks.POWERED_MAGLEV_RAIL.stream());
        valueLookupBuilder(BlockTags.RAILS)
                .forceAddTag(MaglevBlocks.MAGLEV_RAILS);
    }
}
