package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

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
}
