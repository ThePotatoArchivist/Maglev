package archives.tater.maglev.mixin;

import archives.tater.maglev.block.VariantPoweredRail;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {
    @WrapOperation(
            method = "isSameRailWithPower(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ZILnet/minecraft/world/level/block/state/properties/RailShape;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean checkVariantType(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) ||
                block instanceof VariantPoweredRail rail1 &&
                instance.getBlock() instanceof VariantPoweredRail rail2 &&
                        rail1.getVariantRailType() == rail2.getVariantRailType();
    }
}
