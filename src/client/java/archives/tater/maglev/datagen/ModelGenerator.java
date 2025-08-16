package archives.tater.maglev.datagen;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ModelGenerator extends FabricModelProvider {
    public static @Nullable Block childBlock;
    public static @Nullable RailModels railModels;

    public static final TextureKey RAIL_EMISSIVE = TextureKey.of("rail_emissive");

    public static final Model RAIL_FLAT_EMISSIVE = new Model(Optional.of(Maglev.id("block/rail_flat_emissive")), Optional.empty(), RAIL_EMISSIVE);
    public static final Model RAIL_CURVED_EMISSIVE = new Model(Optional.of(Maglev.id("block/rail_curved_emissive")), Optional.empty(), RAIL_EMISSIVE);
    public static final Model RAIL_RAISED_NE_EMISSIVE = new Model(Optional.of(Maglev.id("block/template_rail_raised_ne_emissive")), Optional.empty(), RAIL_EMISSIVE);
    public static final Model RAIL_RAISED_SW_EMISSIVE = new Model(Optional.of(Maglev.id("block/template_rail_raised_sw_emissive")), Optional.empty(), RAIL_EMISSIVE);

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
                    texture == null ? Models.RAIL_FLAT : new Model(Optional.of(RAIL_FLAT_EMISSIVE.upload(name.withPath(path -> "block/" + path + "_flat"), TextureMap.of(RAIL_EMISSIVE, texture.withPrefixedPath("block/")), modelGenerator.modelCollector)), Optional.empty(), TextureKey.RAIL),
                    texture == null ? Models.RAIL_CURVED : new Model(Optional.of(RAIL_CURVED_EMISSIVE.upload(name.withPath(path -> "block/" + path + "_curved"), TextureMap.of(RAIL_EMISSIVE, texture.withPath(path -> "block/" + path + "_corner")), modelGenerator.modelCollector)), Optional.of("_corner"), TextureKey.RAIL),
                    texture == null ? Models.TEMPLATE_RAIL_RAISED_NE : new Model(Optional.of(RAIL_RAISED_NE_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_raised_ne"), TextureMap.of(RAIL_EMISSIVE, texture.withPrefixedPath("block/")), modelGenerator.modelCollector)), Optional.of("_raised_ne"), TextureKey.RAIL),
                    texture == null ? Models.TEMPLATE_RAIL_RAISED_SW : new Model(Optional.of(RAIL_RAISED_SW_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_raised_sw"), TextureMap.of(RAIL_EMISSIVE, texture.withPrefixedPath("block/")), modelGenerator.modelCollector)), Optional.of("_raised_se"), TextureKey.RAIL),
                    new Model(Optional.of(RAIL_FLAT_EMISSIVE.upload(name.withPath(path -> "block/" + path + "_on_flat"), TextureMap.of(RAIL_EMISSIVE, onTexture.withPrefixedPath("block/")), modelGenerator.modelCollector)), Optional.empty(), TextureKey.RAIL),
                    new Model(Optional.of(RAIL_RAISED_NE_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_on_raised_ne"), TextureMap.of(RAIL_EMISSIVE, onTexture.withPrefixedPath("block/")), modelGenerator.modelCollector)), Optional.of("_raised_ne"), TextureKey.RAIL),
                    new Model(Optional.of(RAIL_RAISED_SW_EMISSIVE.upload(name.withPath(path -> "block/template_" + path + "_on_raised_sw"), TextureMap.of(RAIL_EMISSIVE, onTexture.withPrefixedPath("block/")), modelGenerator.modelCollector)), Optional.of("_raised_sw"), TextureKey.RAIL)
            );
        }
    }

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

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    private static void registerOxidizableStraightRail(BlockStateModelGenerator modelGenerator, MaglevBlocks.OxidizableBlockSet blockSet, RailModels railModels) {
        registerStraightRail(modelGenerator, blockSet.base(), blockSet.waxedBase(), railModels);
        registerStraightRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed(), railModels);
        registerStraightRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized(), railModels);
        registerStraightRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered(), railModels);
    }

    private static void registerOxidizableTurnableRail(BlockStateModelGenerator modelGenerator, MaglevBlocks.OxidizableBlockSet blockSet, RailModels railModels) {
        registerTurnableRail(modelGenerator, blockSet.base(), blockSet.waxedBase(), railModels);
        registerTurnableRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed(), railModels);
        registerTurnableRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized(), railModels);
        registerTurnableRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered(), railModels);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        var baseModels = RailModels.createEmissive(blockStateModelGenerator, Maglev.id("maglev_rail"), Maglev.id("maglev_rail_glow"), Maglev.id("powered_maglev_rail_glow"));
        var variableModels = RailModels.createEmissive(blockStateModelGenerator, Maglev.id("variable_maglev_rail"), null, Maglev.id("variable_maglev_rail_on_glow"));

        registerOxidizableTurnableRail(blockStateModelGenerator, MaglevBlocks.MAGLEV_RAIL, baseModels);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.VARIABLE_MAGLEV_RAIL, variableModels);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.POWERED_MAGLEV_RAIL, baseModels);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
