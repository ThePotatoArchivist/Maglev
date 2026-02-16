package archives.tater.maglev.datagen;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevBlocks.WeatheringCopperBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
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

    // Internal mixin data

    @ApiStatus.Internal
    public static @Nullable Block childBlock;

    // Mixined utilities

    public static void registerStraightRail(BlockModelGenerators modelGenerator, Block block, Block child) {
        childBlock = child;
        modelGenerator.createActiveRail(block);
        modelGenerator.delegateItemModel(child, ModelLocationUtils.getModelLocation(block.asItem()));
    }

    public static void registerTurnableRail(BlockModelGenerators modelGenerator, Block block, Block child) {
        childBlock = child;
        modelGenerator.createPassiveRail(block);
        modelGenerator.delegateItemModel(child, ModelLocationUtils.getModelLocation(block.asItem()));
    }

    // Specialized utilities

    private static void registerOxidizableStraightRail(BlockModelGenerators modelGenerator, WeatheringCopperBlocks blockSet) {
        registerStraightRail(modelGenerator, blockSet.unaffected(), blockSet.waxed());
        registerStraightRail(modelGenerator, blockSet.exposed(), blockSet.waxedExposed());
        registerStraightRail(modelGenerator, blockSet.oxidized(), blockSet.waxedOxidized());
        registerStraightRail(modelGenerator, blockSet.weathered(), blockSet.waxedWeathered());
    }

    private static void registerOxidizableTurnableRail(BlockModelGenerators modelGenerator, WeatheringCopperBlocks blockSet) {
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
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        registerOxidizableTurnableRail(blockStateModelGenerator, MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        registerOxidizableStraightRail(blockStateModelGenerator, MaglevBlocks.POWERED_MAGLEV_RAIL);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }
}
