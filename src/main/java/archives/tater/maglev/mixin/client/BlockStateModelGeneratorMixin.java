package archives.tater.maglev.mixin.client;

import archives.tater.maglev.datagen.ModelGenerator;

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

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@Mixin(BlockModelGenerators.class)
public class BlockStateModelGeneratorMixin {

    @Shadow
    @Final
    public Consumer<BlockStateGenerator> blockStateOutput;

    @Inject(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At("HEAD")
	)
	private void popBlock(Block rail, CallbackInfo ci, @Share("targetBlock") LocalRef<Block> targetBlock) {
		targetBlock.set(ModelGenerator.childBlock);
		ModelGenerator.childBlock = null;
	}

	@WrapOperation(
			method = {
					"createPassiveRail",
					"createActiveRail"
			},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/data/models/blockstates/MultiVariantGenerator;with(Lnet/minecraft/data/models/blockstates/PropertyDispatch;)Lnet/minecraft/data/models/blockstates/MultiVariantGenerator;")
	)
	private MultiVariantGenerator replaceBlock2(MultiVariantGenerator instance, PropertyDispatch map, Operation<MultiVariantGenerator> original, @Share("targetBlock") LocalRef<Block> targetBlock) {
		var originalResult = original.call(instance, map);
		if (targetBlock.get() == null) return originalResult;

		blockStateOutput.accept(MultiVariantGenerator.multiVariant(targetBlock.get()).with(map));

		return originalResult;
	}
}