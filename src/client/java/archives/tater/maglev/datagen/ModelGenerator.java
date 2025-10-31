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

    // Models

    public static final TextureKey RAIL_EMISSIVE = TextureKey.of("rail_emissive");

    public static final Model RAIL_FLAT_EMISSIVE = blockModel("rail_flat_emissive", RAIL_EMISSIVE);
    public static final Model RAIL_CURVED_EMISSIVE = blockModel("rail_curved_emissive", RAIL_EMISSIVE);
    public static final Model RAIL_RAISED_NE_EMISSIVE = blockModel("template_rail_raised_ne_emissive", RAIL_EMISSIVE);
    public static final Model RAIL_RAISED_SW_EMISSIVE = blockModel("template_rail_raised_sw_emissive", RAIL_EMISSIVE);

    // Data

    public record RailModels(
            Model flat,
            Model curved,
            Model raisedNE,
            Model raisedSW,
            Model flatOn,
            Model raisedNEOn,
            Model raisedSWOn
    ) {
        public static RailModels createEmissive(BlockStateModelGenerator modelGenerator, Identifier name, @Nullable Identifier texture, Identifier onTexture) {
            return new RailModels(

                    texture == null ? Models.RAIL_FLAT : model(
                            RAIL_FLAT_EMISSIVE.upload(name.withPath(path -> "block/" + path + "_flat"), TextureMap.of(RAIL_EMISSIVE, texture.withPrefixedPath("block/")), modelGenerator.modelCollector),
                            TextureKey.RAIL),

                    texture == null ? Models.RAIL_CURVED : model(
                            RAIL_CURVED_EMISSIVE.upload(name.withPath(path -> "block/" + path + "_curved"), TextureMap.of(RAIL_EMISSIVE, texture.withPath(path -> "block/" + path + "_corner")), modelGenerator.modelCollector),
                            "_corner", TextureKey.RAIL),

                    texture == null ? Models.TEMPLATE_RAIL_RAISED_NE : model(
                            RAIL_RAISED_NE_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_raised_ne"), TextureMap.of(RAIL_EMISSIVE, texture.withPrefixedPath("block/")), modelGenerator.modelCollector),
                            "_raised_ne", TextureKey.RAIL),

                    texture == null ? Models.TEMPLATE_RAIL_RAISED_SW : model(
                            RAIL_RAISED_SW_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_raised_sw"), TextureMap.of(RAIL_EMISSIVE, texture.withPrefixedPath("block/")), modelGenerator.modelCollector),
                            "_raised_se", TextureKey.RAIL),

                    model(
                            RAIL_FLAT_EMISSIVE.upload(name.withPath(path -> "block/" + path + "_on_flat"), TextureMap.of(RAIL_EMISSIVE, onTexture.withPrefixedPath("block/")), modelGenerator.modelCollector),
                            TextureKey.RAIL),

                    model(
                            RAIL_RAISED_NE_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_on_raised_ne"), TextureMap.of(RAIL_EMISSIVE, onTexture.withPrefixedPath("block/")), modelGenerator.modelCollector),
                            "_raised_ne", TextureKey.RAIL),

                    model(
                            RAIL_RAISED_SW_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_on_raised_sw"), TextureMap.of(RAIL_EMISSIVE, onTexture.withPrefixedPath("block/")), modelGenerator.modelCollector),
                            "_raised_sw", TextureKey.RAIL)
            );
        }
    }

    // Internal mixin data

    @ApiStatus.Internal
    public static @Nullable Block childBlock;
    @ApiStatus.Internal
    public static @Nullable RailModels railModels;

    // Mixined utilities

    public static void registerStraightRail(BlockStateModelGenerator modelGenerator, Block block, Block child, RailModels railModels) {
        childBlock = child;
        ModelGenerator.railModels = railModels;
        modelGenerator.registerStraightRail(block);
        modelGenerator.registerParentedItemModel(child, ModelIds.getItemModelId(block.asItem()));
    }

    public static void registerTurnableRail(BlockStateModelGenerator modelGenerator, Block block, Block child, RailModels railModels) {
        childBlock = child;
        ModelGenerator.railModels = railModels;
        modelGenerator.registerTurnableRail(block);
        modelGenerator.registerParentedItemModel(child, ModelIds.getItemModelId(block.asItem()));
    }

    // Specialized utilities

    private static void registerOxidizableStraightRail(BlockStateModelGenerator modelGenerator, CopperBlockSet blockSet, RailModels railModels) {
        registerStraightRail(modelGenerator, blockSet.unaffected(), blockSet.waxed(), railModels);
        registerStraightRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed(), railModels);
        registerStraightRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized(), railModels);
        registerStraightRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered(), railModels);
    }

    private static void registerOxidizableTurnableRail(BlockStateModelGenerator modelGenerator, CopperBlockSet blockSet, RailModels railModels) {
        registerTurnableRail(modelGenerator, blockSet.unaffected(), blockSet.waxed(), railModels);
        registerTurnableRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed(), railModels);
        registerTurnableRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized(), railModels);
        registerTurnableRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered(), railModels);
    }

    // Datagen implementation

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        var baseModels = RailModels.createEmissive(blockStateModelGenerator, Maglev.id("maglev_rail"), Maglev.id("maglev_rail_glow"), Maglev.id("powered_maglev_rail_glow"));
        var variableModels = RailModels.createEmissive(blockStateModelGenerator, Maglev.id("variable_maglev_rail"), null, Maglev.id("variable_maglev_rail_glow"));

        registerOxidizableTurnableRail(blockStateModelGenerator, MaglevBlocks.MAGLEV_RAIL, baseModels);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.VARIABLE_MAGLEV_RAIL, variableModels);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.POWERED_MAGLEV_RAIL, baseModels);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
