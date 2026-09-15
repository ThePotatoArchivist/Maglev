package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlockItemTags;
import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

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
        copy(MaglevBlockItemTags.MAGLEV_RAILS);
        copy(MaglevBlockItemTags.POWERED_MAGLEV_RAILS);
        copy(MaglevBlockItemTags.VARIABLE_MAGLEV_RAILS);
        copy(MaglevBlockItemTags.HOVERABLE_RAILS);
        copy(BlockTags.RAILS, ItemTags.RAILS);
        builder(MaglevItems.OXIDIZERS)
                .add(BlockItemIds.WET_SPONGE);
    }
}
