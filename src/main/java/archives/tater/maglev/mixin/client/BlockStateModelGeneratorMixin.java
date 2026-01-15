package archives.tater.maglev.mixin.client;

import archives.tater.maglev.datagen.ModelGenerator;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.world.level.block.Block;

import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

@Mixin(BlockModelGenerators.class)
public class BlockStateModelGeneratorMixin {
	@Shadow @Final public Consumer<BlockModelDefinitionGenerator> blockStateOutput;

	@Inject(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At("HEAD")
	)
	private void popBlock(Block rail, CallbackInfo ci, @Share("targetBlock") LocalRef<@Nullable Block> targetBlock, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		targetBlock.set(ModelGenerator.childBlock);
		railModels.set(ModelGenerator.railModels);
		ModelGenerator.childBlock = null;
		ModelGenerator.railModels = null;
	}

	@WrapOperation(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Empty;with(Lnet/minecraft/client/data/models/blockstates/PropertyDispatch;)Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator;")
	)
	private MultiVariantGenerator replaceBlock2(MultiVariantGenerator.Empty instance, PropertyDispatch<MultiVariant> variantMap, Operation<MultiVariantGenerator> original, @Share("targetBlock") LocalRef<@Nullable Block> targetBlock) {
		var originalResult = original.call(instance, variantMap);
        var targetBlockValue = targetBlock.get();
        if (targetBlockValue == null) return originalResult;

		blockStateOutput.accept(MultiVariantGenerator.dispatch(targetBlockValue).with(variantMap));

		return originalResult;
	}

	@ModifyExpressionValue(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_FLAT:Lnet/minecraft/client/data/models/model/ModelTemplate;", ordinal = 0)
	)
	private ModelTemplate replaceModel1(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
        return models == null ? original : models.flat();
    }

	@ModifyExpressionValue(
			method = "createActiveRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_FLAT:Lnet/minecraft/client/data/models/model/ModelTemplate;", ordinal = 1)
	)
	private ModelTemplate replaceModel2(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.flatOn();
	}

	@ModifyExpressionValue(
			method = "createPassiveRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_CURVED:Lnet/minecraft/client/data/models/model/ModelTemplate;")
	)
	private ModelTemplate replaceModel3(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.curved();
	}

	@ModifyExpressionValue(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_RAISED_NE:Lnet/minecraft/client/data/models/model/ModelTemplate;", ordinal = 0)
	)
	private ModelTemplate replaceModel4(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedNE();
	}

	@ModifyExpressionValue(
			method = "createActiveRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_RAISED_NE:Lnet/minecraft/client/data/models/model/ModelTemplate;", ordinal = 1)
	)
	private ModelTemplate replaceModel5(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedNEOn();
	}

	@ModifyExpressionValue(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_RAISED_SW:Lnet/minecraft/client/data/models/model/ModelTemplate;", ordinal = 0)
	)
	private ModelTemplate replaceModel6(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedSW();
	}

	@ModifyExpressionValue(
			method = "createActiveRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/client/data/models/model/ModelTemplates;RAIL_RAISED_SW:Lnet/minecraft/client/data/models/model/ModelTemplate;", ordinal = 1)
	)
	private ModelTemplate replaceModel7(ModelTemplate original, @Share("railModels") LocalRef<ModelGenerator.@Nullable RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedSWOn();
	}
}