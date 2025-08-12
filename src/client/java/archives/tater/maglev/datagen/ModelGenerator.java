package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    private static void registerOxidizableRail(BlockStateModelGenerator modelGenerator, MaglevBlocks.OxidizableBlockSet blockSet) {
        modelGenerator.registerStraightRail(blockSet.base());
        modelGenerator.registerStraightRail(blockSet.exposed());
        modelGenerator.registerStraightRail(blockSet.oxidized());
        modelGenerator.registerStraightRail(blockSet.weathered());
        modelGenerator.registerParented(blockSet.base(), blockSet.waxedBase());
        modelGenerator.registerParented(blockSet.exposed(), blockSet.waxedExposed());
        modelGenerator.registerParented(blockSet.oxidized(), blockSet.waxedOxidized());
        modelGenerator.registerParented(blockSet.weathered(), blockSet.waxedWeathered());
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerOxidizableRail(blockStateModelGenerator, MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableRail(blockStateModelGenerator, MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        registerOxidizableRail(blockStateModelGenerator, MaglevBlocks.POWERED_MAGLEV_RAIL);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
