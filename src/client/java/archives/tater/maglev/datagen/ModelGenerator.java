package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ModelIds;
import org.jetbrains.annotations.Nullable;

public class ModelGenerator extends FabricModelProvider {
    public static @Nullable Block childBlock;

    public static void registerStraightRail(BlockStateModelGenerator modelGenerator, Block block, Block child) {
        childBlock = child;
        modelGenerator.registerStraightRail(block);
        modelGenerator.registerParentedItemModel(child, ModelIds.getItemModelId(block.asItem()));
    }

    public static void registerTurnableRail(BlockStateModelGenerator modelGenerator, Block block, Block child) {
        childBlock = child;
        modelGenerator.registerTurnableRail(block);
        modelGenerator.registerParentedItemModel(child, ModelIds.getItemModelId(block.asItem()));
    }

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    private static void registerOxidizableStraightRail(BlockStateModelGenerator modelGenerator, MaglevBlocks.OxidizableBlockSet blockSet) {
        registerStraightRail(modelGenerator, blockSet.base(), blockSet.waxedBase());
        registerStraightRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed());
        registerStraightRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized());
        registerStraightRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered());
    }

    private static void registerOxidizableTurnableRail(BlockStateModelGenerator modelGenerator, MaglevBlocks.OxidizableBlockSet blockSet) {
        registerTurnableRail(modelGenerator, blockSet.base(), blockSet.waxedBase());
        registerTurnableRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed());
        registerTurnableRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized());
        registerTurnableRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered());
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerOxidizableTurnableRail(blockStateModelGenerator, MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.POWERED_MAGLEV_RAIL);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
