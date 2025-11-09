package archives.tater.maglev.datagen;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ModelGenerator extends FabricModelProvider {

    // Static utilities

    private static ModelTemplate model(ResourceLocation parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(parent), Optional.empty(), requiredTextureKeys);
    }

    private static ModelTemplate model(ResourceLocation parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(parent), Optional.of(variant), requiredTextureKeys);
    }

    private static ModelTemplate blockModel(ResourceLocation parent, TextureSlot... requiredTextureKeys) {
        return model(parent.withPrefix("block/"), requiredTextureKeys);
    }

    private static ModelTemplate blockModel(String parent, TextureSlot... requiredTextureKeys) {
        return blockModel(Maglev.id(parent), requiredTextureKeys);
    }

    // Models

    public static final TextureSlot RAIL_EMISSIVE = TextureSlot.create("rail_emissive");

    public static final ModelTemplate RAIL_FLAT_EMISSIVE = blockModel("rail_flat_emissive", RAIL_EMISSIVE);
    public static final ModelTemplate RAIL_CURVED_EMISSIVE = blockModel("rail_curved_emissive", RAIL_EMISSIVE);
    public static final ModelTemplate RAIL_RAISED_NE_EMISSIVE = blockModel("template_rail_raised_ne_emissive", RAIL_EMISSIVE);
    public static final ModelTemplate RAIL_RAISED_SW_EMISSIVE = blockModel("template_rail_raised_sw_emissive", RAIL_EMISSIVE);

    // Data

    public record RailModels(
            ModelTemplate flat,
            ModelTemplate curved,
            ModelTemplate raisedNE,
            ModelTemplate raisedSW,
            ModelTemplate flatOn,
            ModelTemplate raisedNEOn,
            ModelTemplate raisedSWOn
    ) {
        public static RailModels createEmissive(BlockModelGenerators modelGenerator, ResourceLocation name, @Nullable ResourceLocation texture, ResourceLocation onTexture) {
            return new RailModels(

                    texture == null ? ModelTemplates.RAIL_FLAT : model(
                            RAIL_FLAT_EMISSIVE.create(name.withPath(path -> "block/" + path + "_flat"), TextureMapping.singleSlot(RAIL_EMISSIVE, texture.withPrefix("block/")), modelGenerator.modelOutput),
                            TextureSlot.RAIL),

                    texture == null ? ModelTemplates.RAIL_CURVED : model(
                            RAIL_CURVED_EMISSIVE.create(name.withPath(path -> "block/" + path + "_curved"), TextureMapping.singleSlot(RAIL_EMISSIVE, texture.withPath(path -> "block/" + path + "_corner")), modelGenerator.modelOutput),
                            "_corner", TextureSlot.RAIL),

                    texture == null ? ModelTemplates.RAIL_RAISED_NE : model(
                            RAIL_RAISED_NE_EMISSIVE.create(name.withPath(path -> "block/template_" + path + "_raised_ne"), TextureMapping.singleSlot(RAIL_EMISSIVE, texture.withPrefix("block/")), modelGenerator.modelOutput),
                            "_raised_ne", TextureSlot.RAIL),

                    texture == null ? ModelTemplates.RAIL_RAISED_SW : model(
                            RAIL_RAISED_SW_EMISSIVE.create(name.withPath(path -> "block/template_" + path + "_raised_sw"), TextureMapping.singleSlot(RAIL_EMISSIVE, texture.withPrefix("block/")), modelGenerator.modelOutput),
                            "_raised_se", TextureSlot.RAIL),

                    model(
                            RAIL_FLAT_EMISSIVE.create(name.withPath(path -> "block/" + path + "_on_flat"), TextureMapping.singleSlot(RAIL_EMISSIVE, onTexture.withPrefix("block/")), modelGenerator.modelOutput),
                            TextureSlot.RAIL),

                    model(
                            RAIL_RAISED_NE_EMISSIVE.create(name.withPath(path -> "block/template_" + path + "_on_raised_ne"), TextureMapping.singleSlot(RAIL_EMISSIVE, onTexture.withPrefix("block/")), modelGenerator.modelOutput),
                            "_raised_ne", TextureSlot.RAIL),

                    model(
                            RAIL_RAISED_SW_EMISSIVE.create(name.withPath(path -> "block/template_" + path + "_on_raised_sw"), TextureMapping.singleSlot(RAIL_EMISSIVE, onTexture.withPrefix("block/")), modelGenerator.modelOutput),
                            "_raised_sw", TextureSlot.RAIL)
            );
        }
    }

    // Internal mixin data

    @ApiStatus.Internal
    public static @Nullable Block childBlock;
    @ApiStatus.Internal
    public static @Nullable RailModels railModels;

    // Mixined utilities

    public static void registerStraightRail(BlockModelGenerators modelGenerator, Block block, Block child, RailModels railModels) {
        childBlock = child;
        ModelGenerator.railModels = railModels;
        modelGenerator.createActiveRail(block);
        modelGenerator.registerSimpleItemModel(child, ModelLocationUtils.getModelLocation(block.asItem()));
    }

    public static void registerTurnableRail(BlockModelGenerators modelGenerator, Block block, Block child, RailModels railModels) {
        childBlock = child;
        ModelGenerator.railModels = railModels;
        modelGenerator.createPassiveRail(block);
        modelGenerator.registerSimpleItemModel(child, ModelLocationUtils.getModelLocation(block.asItem()));
    }

    // Specialized utilities

    private static void registerOxidizableStraightRail(BlockModelGenerators modelGenerator, WeatheringCopperBlocks blockSet, RailModels railModels) {
        registerStraightRail(modelGenerator, blockSet.unaffected(), blockSet.waxed(), railModels);
        registerStraightRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed(), railModels);
        registerStraightRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized(), railModels);
        registerStraightRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered(), railModels);
    }

    private static void registerOxidizableTurnableRail(BlockModelGenerators modelGenerator, WeatheringCopperBlocks blockSet, RailModels railModels) {
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
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        var baseModels = RailModels.createEmissive(blockStateModelGenerator, Maglev.id("maglev_rail"), Maglev.id("maglev_rail_glow"), Maglev.id("powered_maglev_rail_glow"));
        var variableModels = RailModels.createEmissive(blockStateModelGenerator, Maglev.id("variable_maglev_rail"), null, Maglev.id("variable_maglev_rail_glow"));

        registerOxidizableTurnableRail(blockStateModelGenerator, MaglevBlocks.MAGLEV_RAIL, baseModels);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.VARIABLE_MAGLEV_RAIL, variableModels);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.POWERED_MAGLEV_RAIL, baseModels);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }
}
