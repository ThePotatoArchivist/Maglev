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

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;

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
	private void popBlock(Block rail, CallbackInfo ci, @Share("targetBlock") LocalRef<Block> targetBlock) {
		targetBlock.set(ModelGenerator.childBlock);
		ModelGenerator.childBlock = null;
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
}