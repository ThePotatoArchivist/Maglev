package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Util.createTranslationKey;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
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
        translationBuilder.add(createTranslationKey("tag", tagKey.id()) + "." + "description", description);
    }

    private static void addDescription(TranslationBuilder translationBuilder, Block block, String description) {
        translationBuilder.add(createTranslationKey("lore", Registries.BLOCK.getId(block)), description);
    }

    private static void add(TranslationBuilder translationBuilder, MaglevBlocks.OxidizableBlockSet blockSet, String name) {
        translationBuilder.add(blockSet.base(), name);
        translationBuilder.add(blockSet.exposed(), "Exposed " + name);
        translationBuilder.add(blockSet.weathered(), "Weathered " + name);
        translationBuilder.add(blockSet.oxidized(), "Oxidized " + name);
        translationBuilder.add(blockSet.waxedBase(), "Waxed " + name);
        translationBuilder.add(blockSet.waxedExposed(), "Waxed Exposed " + name);
        translationBuilder.add(blockSet.waxedWeathered(), "Waxed Weathered " + name);
        translationBuilder.add(blockSet.waxedOxidized(), "Waxed Oxidized " + name);
    }

    private static String createRailDescription(String behavior, String speed) {
        return "A rail block that " + behavior + ". Friction is removed and minecart travel at a " + speed + " max speed.";
    }

    private static void addDescription(TranslationBuilder translationBuilder, MaglevBlocks.OxidizableBlockSet blockSet, String behavior) {
        addDescription(translationBuilder, blockSet.base(), createRailDescription(behavior, "very high"));
        addDescription(translationBuilder, blockSet.exposed(), createRailDescription(behavior, "high"));
        addDescription(translationBuilder, blockSet.weathered(), createRailDescription(behavior, "normal"));
        addDescription(translationBuilder, blockSet.oxidized(), createRailDescription(behavior, "low"));
        addDescription(translationBuilder, blockSet.waxedBase(), createRailDescription(behavior, "very high"));
        addDescription(translationBuilder, blockSet.waxedExposed(), createRailDescription(behavior, "high"));
        addDescription(translationBuilder, blockSet.waxedWeathered(), createRailDescription(behavior, "normal"));
        addDescription(translationBuilder, blockSet.waxedOxidized(), createRailDescription(behavior, "low"));
    }
}
