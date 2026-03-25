package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WeatheringCopperItems;

import java.util.concurrent.CompletableFuture;

public class MaglevRecipeGenerator extends RecipeProvider {
    public MaglevRecipeGenerator(HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
    }

    private void offerWaxingRecipe(Item waxed, Item unwaxed) {
        shapeless(RecipeCategory.TRANSPORTATION, waxed)
                .requires(unwaxed)
                .requires(Items.HONEYCOMB)
                .group(getItemName(waxed))
                .unlockedBy(getHasName(unwaxed), has(unwaxed))
                .save(output, getConversionRecipeName(waxed, Items.HONEYCOMB));
    }

    private void offerWaxingRecipes(WeatheringCopperItems blockSet) {
        offerWaxingRecipe(blockSet.waxed(), blockSet.unaffected());
        offerWaxingRecipe(blockSet.waxedExposed(), blockSet.exposed());
        offerWaxingRecipe(blockSet.waxedWeathered(), blockSet.weathered());
        offerWaxingRecipe(blockSet.waxedOxidized(), blockSet.oxidized());
    }

    @Override
    public void buildRecipes() {
        offerWaxingRecipes(MaglevItems.MAGLEV_RAIL);
        offerWaxingRecipes(MaglevItems.POWERED_MAGLEV_RAIL);
        offerWaxingRecipes(MaglevItems.VARIABLE_MAGLEV_RAIL);

        shaped(RecipeCategory.TRANSPORTATION, MaglevItems.MAGLEV_RAIL.unaffected(), 6)
                .pattern("# #")
                .pattern("#%#")
                .pattern("# #")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(ConventionalItemTags.COPPER_INGOTS))
                .save(output);

        shaped(RecipeCategory.TRANSPORTATION, MaglevItems.POWERED_MAGLEV_RAIL.unaffected(), 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .define('&', ConventionalItemTags.GOLD_INGOTS)
                .define('*', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(MaglevItems.MAGLEV_RAIL.unaffected()), has(MaglevItems.MAGLEV_RAILS))
                .save(output);

        shaped(RecipeCategory.TRANSPORTATION, MaglevItems.VARIABLE_MAGLEV_RAIL.unaffected(), 1)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .define('&', ConventionalItemTags.QUARTZ_GEMS)
                .define('*', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(MaglevItems.MAGLEV_RAIL.unaffected()), has(MaglevItems.MAGLEV_RAILS))
                .save(output);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
            return new MaglevRecipeGenerator(registryLookup, exporter);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
