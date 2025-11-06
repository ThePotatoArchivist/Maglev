package archives.tater.maglev.datagen;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevBlocks.CopperBlockSet;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ModelGenerator extends FabricModelProvider {

    // Static utilities

    private static Model model(Identifier parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(parent), Optional.empty(), requiredTextureKeys);
    }

    private static Model model(Identifier parent, String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(parent), Optional.of(variant), requiredTextureKeys);
    }

    private static Model blockModel(Identifier parent, TextureKey... requiredTextureKeys) {
        return model(parent.withPrefixedPath("block/"), requiredTextureKeys);
    }

    private static Model blockModel(String parent, TextureKey... requiredTextureKeys) {
        return blockModel(Maglev.id(parent), requiredTextureKeys);
    }

    // Internal mixin data

    @ApiStatus.Internal
    public static @Nullable Block childBlock;

    // Mixined utilities

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

    // Specialized utilities

    private static void registerOxidizableStraightRail(BlockStateModelGenerator modelGenerator, CopperBlockSet blockSet) {
        registerStraightRail(modelGenerator, blockSet.unaffected(), blockSet.waxed());
        registerStraightRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed());
        registerStraightRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized());
        registerStraightRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered());
    }

    private static void registerOxidizableTurnableRail(BlockStateModelGenerator modelGenerator, CopperBlockSet blockSet) {
        registerTurnableRail(modelGenerator, blockSet.unaffected(), blockSet.waxed());
        registerTurnableRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed());
        registerTurnableRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized());
        registerTurnableRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered());
    }

    // Datagen implementation

    public ModelGenerator(FabricDataOutput output) {
        super(output);
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
