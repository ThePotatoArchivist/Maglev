package archives.tater.maglev.mixin;

import archives.tater.maglev.block.VariantPoweredRail;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {
    @WrapOperation(
            method = "isPoweredByOtherRails(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZILnet/minecraft/block/enums/RailShape;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z")
    )
    private boolean checkVariantType(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) ||
                block instanceof VariantPoweredRail rail1 &&
                instance.getBlock() instanceof VariantPoweredRail rail2 &&
                        rail1.getVariantRailType() == rail2.getVariantRailType();
    }
}
