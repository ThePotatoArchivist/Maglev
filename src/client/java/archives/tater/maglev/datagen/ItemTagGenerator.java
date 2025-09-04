package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, @Nullable BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    public static FabricDataGenerator.Pack.RegistryDependentFactory<ItemTagGenerator> factory(BlockTagProvider blockTagProvider) {
        return (output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        copy(MaglevBlocks.MAGLEV_RAILS, MaglevItems.MAGLEV_RAILS);
        copy(MaglevBlocks.POWERED_MAGLEV_RAILS, MaglevItems.POWERED_MAGLEV_RAILS);
        copy(MaglevBlocks.VARIABLE_MAGLEV_RAILS, MaglevItems.VARIABLE_MAGLEV_RAILS);
        copy(MaglevBlocks.HOVERABLE_RAILS, MaglevItems.HOVERABLE_RAILS);
        copy(BlockTags.RAILS, ItemTags.RAILS);
        valueLookupBuilder(MaglevItems.OXIDIZERS)
                .add(Items.WET_SPONGE);
    }
}
