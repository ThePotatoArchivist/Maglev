package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlockItemTags;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Util.makeDescriptionId;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        add(translationBuilder, MaglevBlocks.MAGLEV_RAIL, "Maglev Rail");
        add(translationBuilder, MaglevBlocks.POWERED_MAGLEV_RAIL, "Powered Maglev Rail");
        add(translationBuilder, MaglevBlocks.VARIABLE_MAGLEV_RAIL, "Variable Maglev Rail");
        translationBuilder.add(MaglevItems.ITEM_GROUP_NAME, "Maglev");
        translationBuilder.add(MaglevBlockItemTags.MAGLEV_RAILS.item(), "Maglev Rails");
        translationBuilder.add(MaglevBlockItemTags.POWERED_MAGLEV_RAILS.item(), "Powered Maglev Rails");
        translationBuilder.add(MaglevBlockItemTags.VARIABLE_MAGLEV_RAILS.item(), "Variable Maglev Rails");
        translationBuilder.add(MaglevBlockItemTags.HOVERABLE_RAILS.item(), "Hoverable Rails");
        translationBuilder.add(MaglevItems.OXIDIZERS, "Oxidizers");
        addDescription(translationBuilder, MaglevBlocks.MAGLEV_RAIL, "allows minecarts to hover up to 15 blocks above it");
        addDescription(translationBuilder, MaglevBlocks.POWERED_MAGLEV_RAIL, "accelerates minecarts hovering above it");
        addDescription(translationBuilder, MaglevBlocks.VARIABLE_MAGLEV_RAIL, "sets minecarts' hover speed to its redstone power level");
    }

    private static void addDescription(TranslationBuilder translationBuilder, Block block, String description) {
        translationBuilder.add(makeDescriptionId("lore", BuiltInRegistries.BLOCK.getKey(block)), description);
    }

    private static void add(TranslationBuilder translationBuilder, WeatheringCopperCollection<Block> blockSet, String name) {
        translationBuilder.add(blockSet.weathering().unaffected(), name);
        translationBuilder.add(blockSet.weathering().exposed(), "Exposed " + name);
        translationBuilder.add(blockSet.weathering().weathered(), "Weathered " + name);
        translationBuilder.add(blockSet.weathering().oxidized(), "Oxidized " + name);
        translationBuilder.add(blockSet.waxed().unaffected(), "Waxed " + name);
        translationBuilder.add(blockSet.waxed().exposed(), "Waxed Exposed " + name);
        translationBuilder.add(blockSet.waxed().weathered(), "Waxed Weathered " + name);
        translationBuilder.add(blockSet.waxed().oxidized(), "Waxed Oxidized " + name);
    }

    private static String createRailDescription(String behavior, String speed) {
        return "A rail block that " + behavior + ". Friction is removed and minecart travel at a " + speed + " max speed.";
    }

    private static void addDescription(TranslationBuilder translationBuilder, WeatheringCopperCollection<Block> blockSet, String behavior) {
        addDescription(translationBuilder, blockSet.weathering().unaffected(), createRailDescription(behavior, "very high"));
        addDescription(translationBuilder, blockSet.weathering().exposed(), createRailDescription(behavior, "high"));
        addDescription(translationBuilder, blockSet.weathering().weathered(), createRailDescription(behavior, "normal"));
        addDescription(translationBuilder, blockSet.weathering().oxidized(), createRailDescription(behavior, "low"));
        addDescription(translationBuilder, blockSet.waxed().unaffected(), createRailDescription(behavior, "very high"));
        addDescription(translationBuilder, blockSet.waxed().exposed(), createRailDescription(behavior, "high"));
        addDescription(translationBuilder, blockSet.waxed().weathered(), createRailDescription(behavior, "normal"));
        addDescription(translationBuilder, blockSet.waxed().oxidized(), createRailDescription(behavior, "low"));
    }
}
