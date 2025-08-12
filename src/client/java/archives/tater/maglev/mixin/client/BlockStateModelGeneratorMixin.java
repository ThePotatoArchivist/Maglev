package archives.tater.maglev.mixin.client;

import archives.tater.maglev.datagen.ModelGenerator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockModelDefinitionCreator;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.WeightedVariant;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BlockStateModelGenerator.class)
public class BlockStateModelGeneratorMixin {
	@Shadow @Final public Consumer<BlockModelDefinitionCreator> blockStateCollector;

	@Inject(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At("HEAD")
	)
	private void popBlock(Block rail, CallbackInfo ci, @Share("targetBlock") LocalRef<Block> targetBlock) {
		targetBlock.set(ModelGenerator.childBlock);
		ModelGenerator.childBlock = null;
	}

	@WrapOperation(
			method = {
					"registerTurnableRail",
					"registerStraightRail"
			},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/data/VariantsBlockModelDefinitionCreator$Empty;with(Lnet/minecraft/client/data/BlockStateVariantMap;)Lnet/minecraft/client/data/VariantsBlockModelDefinitionCreator;")
	)
	private VariantsBlockModelDefinitionCreator replaceBlock2(VariantsBlockModelDefinitionCreator.Empty instance, BlockStateVariantMap<WeightedVariant> variantMap, Operation<VariantsBlockModelDefinitionCreator> original, @Share("targetBlock") LocalRef<Block> targetBlock) {
		var originalResult = original.call(instance, variantMap);
		if (targetBlock.get() == null) return originalResult;

		blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(targetBlock.get()).with(variantMap));

		return originalResult;
	}
}