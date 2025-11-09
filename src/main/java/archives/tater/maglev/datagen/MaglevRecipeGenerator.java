package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevBlocks.CopperBlockSet;
import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class MaglevRecipeGenerator extends FabricRecipeProvider {

    public MaglevRecipeGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private void offerWaxingRecipe(RecipeOutput exporter, Item waxed, Item unwaxed) {
        new ShapelessRecipeBuilder(RecipeCategory.TRANSPORTATION, waxed, 1)
                .requires(unwaxed)
                .requires(Items.HONEYCOMB)
                .group(getItemName(waxed))
                .unlockedBy(getHasName(unwaxed), has(unwaxed))
                .save(exporter, getConversionRecipeName(waxed, Items.HONEYCOMB));
    }

    private void offerWaxingRecipes(RecipeOutput exporter, CopperBlockSet blockSet) {
        offerWaxingRecipe(exporter, blockSet.waxed().asItem(), blockSet.unaffected().asItem());
        offerWaxingRecipe(exporter, blockSet.waxedExposed().asItem(), blockSet.exposed().asItem());
        offerWaxingRecipe(exporter, blockSet.waxedWeathered().asItem(), blockSet.weathered().asItem());
        offerWaxingRecipe(exporter, blockSet.waxedOxidized().asItem(), blockSet.oxidized().asItem());
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        offerWaxingRecipes(exporter, MaglevBlocks.MAGLEV_RAIL);
        offerWaxingRecipes(exporter, MaglevBlocks.POWERED_MAGLEV_RAIL);
        offerWaxingRecipes(exporter, MaglevBlocks.VARIABLE_MAGLEV_RAIL);

        new ShapedRecipeBuilder(RecipeCategory.TRANSPORTATION, MaglevBlocks.MAGLEV_RAIL.unaffected(), 6)
                .pattern("# #")
                .pattern("#%#")
                .pattern("# #")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(ConventionalItemTags.COPPER_INGOTS))
                .save(exporter);

        new ShapedRecipeBuilder(RecipeCategory.TRANSPORTATION, MaglevBlocks.POWERED_MAGLEV_RAIL.unaffected(), 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .define('&', ConventionalItemTags.GOLD_INGOTS)
                .define('*', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(MaglevBlocks.MAGLEV_RAIL.unaffected()), has(MaglevItems.MAGLEV_RAILS))
                .save(exporter);

        new ShapedRecipeBuilder(RecipeCategory.TRANSPORTATION, MaglevBlocks.VARIABLE_MAGLEV_RAIL.unaffected(), 1)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .define('#', ConventionalItemTags.COPPER_INGOTS)
                .define('%', ConventionalItemTags.IRON_INGOTS)
                .define('&', ConventionalItemTags.QUARTZ_GEMS)
                .define('*', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(MaglevBlocks.MAGLEV_RAIL.unaffected()), has(MaglevItems.MAGLEV_RAILS))
                .save(exporter);
    }
}
