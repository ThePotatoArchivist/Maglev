package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class MaglevRecipeGenerator extends RecipeGenerator {
    public MaglevRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    private void offerWaxingRecipe(Item waxed, Item unwaxed) {
        createShapeless(RecipeCategory.TRANSPORTATION, waxed)
                .input(unwaxed)
                .input(Items.HONEYCOMB)
                .group(getItemPath(waxed))
                .criterion(hasItem(unwaxed), conditionsFromItem(unwaxed))
                .offerTo(exporter, convertBetween(waxed, Items.HONEYCOMB));
    }

    private void offerWaxingRecipes(MaglevBlocks.OxidizableBlockSet blockSet) {
        offerWaxingRecipe(blockSet.waxedBase().asItem(), blockSet.base().asItem());
        offerWaxingRecipe(blockSet.waxedExposed().asItem(), blockSet.exposed().asItem());
        offerWaxingRecipe(blockSet.waxedWeathered().asItem(), blockSet.weathered().asItem());
        offerWaxingRecipe(blockSet.waxedOxidized().asItem(), blockSet.oxidized().asItem());
    }

    @Override
    public void generate() {
        offerWaxingRecipes(MaglevBlocks.MAGLEV_RAIL);
        offerWaxingRecipes(MaglevBlocks.POWERED_MAGLEV_RAIL);
        offerWaxingRecipes(MaglevBlocks.VARIABLE_MAGLEV_RAIL);

        createShaped(RecipeCategory.TRANSPORTATION, MaglevBlocks.MAGLEV_RAIL.base(), 3)
                .pattern("# #")
                .pattern("#%#")
                .pattern("# #")
                .input('#', ConventionalItemTags.COPPER_INGOTS)
                .input('%', Items.ECHO_SHARD) // TODO
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromTag(ConventionalItemTags.COPPER_INGOTS))
                .offerTo(exporter);

        createShaped(RecipeCategory.TRANSPORTATION, MaglevBlocks.POWERED_MAGLEV_RAIL.base(), 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .input('#', ConventionalItemTags.COPPER_INGOTS)
                .input('%', Items.ECHO_SHARD) // TODO
                .input('&', ConventionalItemTags.GOLD_INGOTS)
                .input('*', ConventionalItemTags.REDSTONE_DUSTS)
                .criterion(hasItem(MaglevBlocks.MAGLEV_RAIL.base()), conditionsFromTag(MaglevItems.MAGLEV_RAILS))
                .offerTo(exporter);

        createShaped(RecipeCategory.TRANSPORTATION, MaglevBlocks.VARIABLE_MAGLEV_RAIL.base(), 1)
                .pattern("# #")
                .pattern("#%#")
                .pattern("#*#")
                .input('#', ConventionalItemTags.COPPER_INGOTS)
                .input('%', Items.ECHO_SHARD) // TODO
                .input('*', ConventionalItemTags.QUARTZ_GEMS)
                .criterion(hasItem(MaglevBlocks.MAGLEV_RAIL.base()), conditionsFromTag(MaglevItems.MAGLEV_RAILS))
                .offerTo(exporter);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
            return new MaglevRecipeGenerator(registryLookup, exporter);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
