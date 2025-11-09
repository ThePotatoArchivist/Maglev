package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopperBlocks;
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

    private void offerWaxingRecipes(WeatheringCopperBlocks blockSet) {
        offerWaxingRecipe(blockSet.waxed().asItem(), blockSet.unaffected().asItem());
        offerWaxingRecipe(blockSet.waxedExposed().asItem(), blockSet.exposed().asItem());
        offerWaxingRecipe(blockSet.waxedWeathered().asItem(), blockSet.weathered().asItem());
        offerWaxingRecipe(blockSet.waxedOxidized().asItem(), blockSet.oxidized().asItem());
    }

    @Override
    public void buildRecipes() {
        offerWaxingRecipes(MaglevBlocks.MAGLEV_RAIL);
        offerWaxingRecipes(MaglevBlocks.POWERED_MAGLEV_RAIL);
        offerWaxingRecipes(MaglevBlocks.VARIABLE_MAGLEV_RAIL);

        shaped(RecipeCategory.TRANSPORTATION, MaglevBlocks.MAGLEV_RAIL.unaffected(), 6)
                .pattern("# #")
                .pattern("#%#")
                .pattern("# #")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(ConventionalItemTags.COPPER_INGOTS))
                .save(output);

        shaped(RecipeCategory.TRANSPORTATION, MaglevBlocks.POWERED_MAGLEV_RAIL.unaffected(), 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .define('&', ConventionalItemTags.GOLD_INGOTS)
                .define('*', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(MaglevBlocks.MAGLEV_RAIL.unaffected()), has(MaglevItems.MAGLEV_RAILS))
                .save(output);

        shaped(RecipeCategory.TRANSPORTATION, MaglevBlocks.VARIABLE_MAGLEV_RAIL.unaffected(), 1)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .define('&', ConventionalItemTags.QUARTZ_GEMS)
                .define('*', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(MaglevBlocks.MAGLEV_RAIL.unaffected()), has(MaglevItems.MAGLEV_RAILS))
                .save(output);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
