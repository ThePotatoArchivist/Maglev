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

import net.minecraft.block.Block;
import net.minecraft.data.client.*;

import java.util.function.Consumer;

@Mixin(BlockStateModelGenerator.class)
public class BlockStateModelGeneratorMixin {

    @Shadow
    @Final
    public Consumer<BlockStateSupplier> blockStateCollector;

    @Inject(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At("HEAD")
	)
	private void popBlock(Block rail, CallbackInfo ci, @Share("targetBlock") LocalRef<Block> targetBlock, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		targetBlock.set(ModelGenerator.childBlock);
		railModels.set(ModelGenerator.railModels);
		ModelGenerator.childBlock = null;
		ModelGenerator.railModels = null;
	}

	@WrapOperation(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/data/client/VariantsBlockStateSupplier;coordinate(Lnet/minecraft/data/client/BlockStateVariantMap;)Lnet/minecraft/data/client/VariantsBlockStateSupplier;")
	)
	private VariantsBlockStateSupplier replaceBlock2(VariantsBlockStateSupplier instance, BlockStateVariantMap map, Operation<VariantsBlockStateSupplier> original, @Share("targetBlock") LocalRef<Block> targetBlock) {
		var originalResult = original.call(instance, map);
		if (targetBlock.get() == null) return originalResult;

		blockStateCollector.accept(VariantsBlockStateSupplier.create(targetBlock.get()).coordinate(map));

		return originalResult;
	}

	@ModifyExpressionValue(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;RAIL_FLAT:Lnet/minecraft/data/client/Model;", ordinal = 0)
	)
	private Model replaceModel1(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
        return models == null ? original : models.flat();
    }

	@ModifyExpressionValue(
			method = "registerStraightRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;RAIL_FLAT:Lnet/minecraft/data/client/Model;", ordinal = 1)
	)
	private Model replaceModel2(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.flatOn();
	}

	@ModifyExpressionValue(
			method = "registerTurnableRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;RAIL_CURVED:Lnet/minecraft/data/client/Model;")
	)
	private Model replaceModel3(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.curved();
	}

	@ModifyExpressionValue(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;TEMPLATE_RAIL_RAISED_NE:Lnet/minecraft/data/client/Model;", ordinal = 0)
	)
	private Model replaceModel4(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedNE();
	}

	@ModifyExpressionValue(
			method = "registerStraightRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;TEMPLATE_RAIL_RAISED_NE:Lnet/minecraft/data/client/Model;", ordinal = 1)
	)
	private Model replaceModel5(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedNEOn();
	}

	@ModifyExpressionValue(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;TEMPLATE_RAIL_RAISED_SW:Lnet/minecraft/data/client/Model;", ordinal = 0)
	)
	private Model replaceModel6(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedSW();
	}

	@ModifyExpressionValue(
			method = "registerStraightRail",
			at = @At(value = "FIELD", target = "Lnet/minecraft/data/client/Models;TEMPLATE_RAIL_RAISED_SW:Lnet/minecraft/data/client/Model;", ordinal = 1)
	)
	private Model replaceModel7(Model original, @Share("railModels") LocalRef<ModelGenerator.RailModels> railModels) {
		var models = railModels.get();
		return models == null ? original : models.raisedSWOn();
	}
}