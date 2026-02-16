package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevBlocks.WeatheringCopperBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.Util.makeDescriptionId;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        add(translationBuilder, MaglevBlocks.MAGLEV_RAIL, "Maglev Rail");
        add(translationBuilder, MaglevBlocks.POWERED_MAGLEV_RAIL, "Powered Maglev Rail");
        add(translationBuilder, MaglevBlocks.VARIABLE_MAGLEV_RAIL, "Variable Maglev Rail");
        translationBuilder.add(MaglevItems.ITEM_GROUP_NAME, "Maglev");
        translationBuilder.add(MaglevItems.MAGLEV_RAILS, "Maglev Rails");
        translationBuilder.add(MaglevItems.POWERED_MAGLEV_RAILS, "Powered Maglev Rails");
        translationBuilder.add(MaglevItems.VARIABLE_MAGLEV_RAILS, "Variable Maglev Rails");
        translationBuilder.add(MaglevItems.HOVERABLE_RAILS, "Hoverable Rails");
        translationBuilder.add(MaglevItems.OXIDIZERS, "Oxidizers");
        addDescription(translationBuilder, MaglevBlocks.MAGLEV_RAIL, "allows minecarts to hover up to 15 blocks above it");
        addDescription(translationBuilder, MaglevBlocks.POWERED_MAGLEV_RAIL, "accelerates minecarts hovering above it");
        addDescription(translationBuilder, MaglevBlocks.VARIABLE_MAGLEV_RAIL, "sets minecarts' hover speed to its redstone power level");
    }

    private static void addDescription(TranslationBuilder translationBuilder, TagKey<?> tagKey, String description) {
        translationBuilder.add(makeDescriptionId("tag", tagKey.location()) + "." + "description", description);
    }

    private static void addDescription(TranslationBuilder translationBuilder, Block block, String description) {
        translationBuilder.add(makeDescriptionId("lore", BuiltInRegistries.BLOCK.getKey(block)), description);
    }

    private static void add(TranslationBuilder translationBuilder, WeatheringCopperBlocks blockSet, String name) {
        translationBuilder.add(blockSet.unaffected(), name);
        translationBuilder.add(blockSet.exposed(), "Exposed " + name);
        translationBuilder.add(blockSet.weathered(), "Weathered " + name);
        translationBuilder.add(blockSet.oxidized(), "Oxidized " + name);
        translationBuilder.add(blockSet.waxed(), "Waxed " + name);
        translationBuilder.add(blockSet.waxedExposed(), "Waxed Exposed " + name);
        translationBuilder.add(blockSet.waxedWeathered(), "Waxed Weathered " + name);
        translationBuilder.add(blockSet.waxedOxidized(), "Waxed Oxidized " + name);
    }

    private static String createRailDescription(String behavior, String speed) {
        return "A rail block that " + behavior + ". Friction is removed and minecart travel at a " + speed + " max speed.";
    }

    private static void addDescription(TranslationBuilder translationBuilder, WeatheringCopperBlocks blockSet, String behavior) {
        addDescription(translationBuilder, blockSet.unaffected(), createRailDescription(behavior, "very high"));
        addDescription(translationBuilder, blockSet.exposed(), createRailDescription(behavior, "high"));
        addDescription(translationBuilder, blockSet.weathered(), createRailDescription(behavior, "normal"));
        addDescription(translationBuilder, blockSet.oxidized(), createRailDescription(behavior, "low"));
        addDescription(translationBuilder, blockSet.waxed(), createRailDescription(behavior, "very high"));
        addDescription(translationBuilder, blockSet.waxedExposed(), createRailDescription(behavior, "high"));
        addDescription(translationBuilder, blockSet.waxedWeathered(), createRailDescription(behavior, "normal"));
        addDescription(translationBuilder, blockSet.waxedOxidized(), createRailDescription(behavior, "low"));
    }
}
