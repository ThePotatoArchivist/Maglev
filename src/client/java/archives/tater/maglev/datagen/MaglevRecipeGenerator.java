package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevBlocks.CopperBlockSet;
import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import java.util.concurrent.CompletableFuture;

public class MaglevRecipeGenerator extends FabricRecipeProvider {

    public MaglevRecipeGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private void offerWaxingRecipe(RecipeExporter exporter, Item waxed, Item unwaxed) {
        new ShapelessRecipeJsonBuilder(RecipeCategory.TRANSPORTATION, waxed, 1)
                .input(unwaxed)
                .input(Items.HONEYCOMB)
                .group(getItemPath(waxed))
                .criterion(hasItem(unwaxed), conditionsFromItem(unwaxed))
                .offerTo(exporter, convertBetween(waxed, Items.HONEYCOMB));
    }

    private void offerWaxingRecipes(RecipeExporter exporter, CopperBlockSet blockSet) {
        offerWaxingRecipe(exporter, blockSet.waxed().asItem(), blockSet.unaffected().asItem());
        offerWaxingRecipe(exporter, blockSet.waxedExposed().asItem(), blockSet.exposed().asItem());
        offerWaxingRecipe(exporter, blockSet.waxedWeathered().asItem(), blockSet.weathered().asItem());
        offerWaxingRecipe(exporter, blockSet.waxedOxidized().asItem(), blockSet.oxidized().asItem());
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerWaxingRecipes(exporter, MaglevBlocks.MAGLEV_RAIL);
        offerWaxingRecipes(exporter, MaglevBlocks.POWERED_MAGLEV_RAIL);
        offerWaxingRecipes(exporter, MaglevBlocks.VARIABLE_MAGLEV_RAIL);

        new ShapedRecipeJsonBuilder(RecipeCategory.TRANSPORTATION, MaglevBlocks.MAGLEV_RAIL.unaffected(), 6)
                .pattern("# #")
                .pattern("#%#")
                .pattern("# #")
                .input('#', ConventionalItemTags.COPPER_INGOTS)
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromTag(ConventionalItemTags.COPPER_INGOTS))
                .offerTo(exporter);

        new ShapedRecipeJsonBuilder(RecipeCategory.TRANSPORTATION, MaglevBlocks.POWERED_MAGLEV_RAIL.unaffected(), 2)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .input('#', ConventionalItemTags.COPPER_INGOTS)
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('&', ConventionalItemTags.GOLD_INGOTS)
                .input('*', ConventionalItemTags.REDSTONE_DUSTS)
                .criterion(hasItem(MaglevBlocks.MAGLEV_RAIL.unaffected()), conditionsFromTag(MaglevItems.MAGLEV_RAILS))
                .offerTo(exporter);

        new ShapedRecipeJsonBuilder(RecipeCategory.TRANSPORTATION, MaglevBlocks.VARIABLE_MAGLEV_RAIL.unaffected(), 1)
                .pattern("#&#")
                .pattern("#%#")
                .pattern("#*#")
                .input('#', ConventionalItemTags.COPPER_INGOTS)
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('&', ConventionalItemTags.QUARTZ_GEMS)
                .input('*', ConventionalItemTags.REDSTONE_DUSTS)
                .criterion(hasItem(MaglevBlocks.MAGLEV_RAIL.unaffected()), conditionsFromTag(MaglevItems.MAGLEV_RAILS))
                .offerTo(exporter);
    }
}
