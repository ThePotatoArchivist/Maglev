package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {

    public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, @Nullable BlockTagsProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    public static FabricDataGenerator.Pack.RegistryDependentFactory<ItemTagGenerator> factory(BlockTagsProvider blockTagProvider) {
        return (output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        copy(MaglevBlocks.MAGLEV_RAILS, MaglevItems.MAGLEV_RAILS);
        copy(MaglevBlocks.POWERED_MAGLEV_RAILS, MaglevItems.POWERED_MAGLEV_RAILS);
        copy(MaglevBlocks.VARIABLE_MAGLEV_RAILS, MaglevItems.VARIABLE_MAGLEV_RAILS);
        copy(MaglevBlocks.HOVERABLE_RAILS, MaglevItems.HOVERABLE_RAILS);
        copy(BlockTags.RAILS, ItemTags.RAILS);
        valueLookupBuilder(MaglevItems.OXIDIZERS)
                .add(Items.WET_SPONGE);
    }
}
