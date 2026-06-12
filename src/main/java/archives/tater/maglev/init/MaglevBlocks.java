package archives.tater.maglev.init;

import archives.tater.maglev.block.*;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.references.BlockItemId;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.world.level.block.WeatheringCopper.getNext;

public class MaglevBlocks {
    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> constructor, BlockBehaviour.Properties settings) {
        return Blocks.register(id.block(), constructor, settings);
    }

    private static WeatheringCopperCollection<Block> registerOxidizable(WeatheringCopperCollection<Block> blockSet) {
        OxidizableBlocksRegistry.registerWeatheringCopperBlocks(blockSet);
        return blockSet;
    }

    private static <Waxed extends Block, Weathering extends Block & WeatheringCopper> WeatheringCopperCollection<Block> registerOxidizableRails(WeatheringCopperCollection<BlockItemId> ids, BiFunction<WeatherState, BlockBehaviour.Properties, Waxed> waxedConstructor, BiFunction<WeatherState, BlockBehaviour.Properties, Weathering> oxidizableConstructor) {
        return registerOxidizable(WeatheringCopperCollection.registerBlocks(
                ids,
                MaglevBlocks::register,
                waxedConstructor,
                oxidizableConstructor,
                _ -> BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL)
                        .sound(SoundType.COPPER)
        ));
    }

    public static final WeatheringCopperCollection<Block> MAGLEV_RAIL = registerOxidizableRails(MaglevBlockItemIds.MAGLEV_RAIL, WaxedRailBlock::new, WeatheringCopperRailBlock::new);
    public static final WeatheringCopperCollection<Block> POWERED_MAGLEV_RAIL = registerOxidizableRails(MaglevBlockItemIds.POWERED_MAGLEV_RAIL, WaxedPoweredRailBlock::new, WeatheringCopperPoweredRailBlock::new);
    public static final WeatheringCopperCollection<Block> VARIABLE_MAGLEV_RAIL = registerOxidizableRails(MaglevBlockItemIds.VARIABLE_MAGLEV_RAIL, WaxedVariableRailBlock::new, WeatheringCopperVariableRailBlock::new);

    public static void init() {

        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (!player.getItemInHand(hand).is(MaglevItems.OXIDIZERS)) return InteractionResult.PASS;
            var pos = hitResult.getBlockPos();
            var state = level.getBlockState(pos);
            if (!state.is(MaglevBlockItemTags.MANUALLY_OXIDIZABLE.block())) return InteractionResult.PASS;

            var oxidizedBlock = getNext(state.getBlock()).orElse(null);
            if (oxidizedBlock == null) return InteractionResult.FAIL;

            level.setBlockAndUpdate(pos, oxidizedBlock.withPropertiesOf(state));
            level.playSound(player, pos, SoundEvents.BOTTLE_EMPTY, player.getSoundSource());
            if (level instanceof ServerLevel serverWorld) {
                var particlePos = Vec3.atBottomCenterOf(pos);
                serverWorld.sendParticles(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z, 8, 0.25, 0, 0.25, 0);
            }

            return InteractionResult.SUCCESS;
        });
    }
}
