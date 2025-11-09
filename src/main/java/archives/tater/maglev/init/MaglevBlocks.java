package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.block.*;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.WeatheringCopper.getNext;

public class MaglevBlocks {
    private static Block register(String path, Function<BlockBehaviour.Properties, Block> constructor, BlockBehaviour.Properties settings) {
        return Blocks.register(ResourceKey.create(Registries.BLOCK, Maglev.id(path)), constructor.apply(settings));
    }

    private static TagKey<Block> tagOf(String path) {
        return TagKey.create(Registries.BLOCK, Maglev.id(path));
    }

    public record CopperBlockSet(
            Block unaffected,
            Block exposed,
            Block weathered,
            Block oxidized,
            Block waxed,
            Block waxedExposed,
            Block waxedWeathered,
            Block waxedOxidized
    ) {
        public CopperBlockSet {
            OxidizableBlocksRegistry.registerOxidizableBlockPair(unaffected, exposed);
            OxidizableBlocksRegistry.registerOxidizableBlockPair(exposed, weathered);
            OxidizableBlocksRegistry.registerOxidizableBlockPair(weathered, oxidized);
            OxidizableBlocksRegistry.registerWaxableBlockPair(unaffected, waxed);
            OxidizableBlocksRegistry.registerWaxableBlockPair(exposed, waxedExposed);
            OxidizableBlocksRegistry.registerWaxableBlockPair(weathered, waxedWeathered);
            OxidizableBlocksRegistry.registerWaxableBlockPair(oxidized, waxedOxidized);
        }

        public Stream<Block> stream() {
            return Stream.of(unaffected, exposed, weathered, oxidized, waxed, waxedExposed, waxedWeathered, waxedOxidized);
        }

        public List<Block> getAll() {
            return stream().toList();
        }

        public Block[] toArray() {
            return getAll().toArray(new Block[0]);
        }

        public void forEach(Consumer<Block> consumer) {
            stream().forEach(consumer);
        }

        public boolean contains(Block block) {
            return (block == unaffected ||
                    block == exposed ||
                    block == weathered ||
                    block == oxidized ||
                    block == waxed ||
                    block == waxedExposed ||
                    block == waxedWeathered ||
                    block == waxedOxidized
            );
        }

        public static Stream<Function<CopperBlockSet, Block>> fields() {
            return Stream.of(
                    CopperBlockSet::unaffected,
                    CopperBlockSet::exposed,
                    CopperBlockSet::weathered,
                    CopperBlockSet::oxidized,
                    CopperBlockSet::waxed,
                    CopperBlockSet::waxedExposed,
                    CopperBlockSet::waxedWeathered,
                    CopperBlockSet::waxedOxidized
            );
        }
    }

    private static String getOxidizedName(String name, WeatherState oxidationLevel) {
        return oxidationLevel == WeatherState.UNAFFECTED ? name : oxidationLevel.getSerializedName() + "_" + name;
    }

    private static Block registerOxidizableRail(String name, WeatherState oxidationLevel, BiFunction<WeatherState, BlockBehaviour.Properties, Block> constructor) {
        return register(
                getOxidizedName(name, oxidationLevel),
                settings -> constructor.apply(oxidationLevel, settings),
                BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL)
        );
    }

    private static Block registerWaxedRail(String name, WeatherState oxidationLevel, BiFunction<WeatherState, BlockBehaviour.Properties, Block> constructor) {
        return register(
                "waxed_" + getOxidizedName(name, oxidationLevel),
                settings -> constructor.apply(oxidationLevel, settings),
                BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL)
        );
    }

    private static CopperBlockSet registerOxidizableRails(String name, BiFunction<WeatherState, BlockBehaviour.Properties, Block> waxedConstructor, BiFunction<WeatherState, BlockBehaviour.Properties, Block> oxidizableConstructor) {
        return new CopperBlockSet(
                registerOxidizableRail(name, WeatherState.UNAFFECTED, oxidizableConstructor),
                registerOxidizableRail(name, WeatherState.EXPOSED, oxidizableConstructor),
                registerOxidizableRail(name, WeatherState.WEATHERED, oxidizableConstructor),
                registerOxidizableRail(name, WeatherState.OXIDIZED, oxidizableConstructor),
                registerWaxedRail(name, WeatherState.UNAFFECTED, waxedConstructor),
                registerWaxedRail(name, WeatherState.EXPOSED, waxedConstructor),
                registerWaxedRail(name, WeatherState.WEATHERED, waxedConstructor),
                registerWaxedRail(name, WeatherState.OXIDIZED, waxedConstructor)
        );
    }

    public static final CopperBlockSet MAGLEV_RAIL = registerOxidizableRails("maglev_rail", WaxedRailBlock::new, OxidizableRailBlock::new);
    public static final CopperBlockSet POWERED_MAGLEV_RAIL = registerOxidizableRails("powered_maglev_rail", WaxedPoweredRailBlock::new, OxidizablePoweredRailBlock::new);
    public static final CopperBlockSet VARIABLE_MAGLEV_RAIL = registerOxidizableRails("variable_maglev_rail", WaxedVariableRailBlock::new, OxidizableVariableRailBlock::new);

    public static final TagKey<Block> MAGLEV_RAILS = tagOf("maglev_rails");
    public static final TagKey<Block> POWERED_MAGLEV_RAILS = tagOf("powered_maglev_rails");
    public static final TagKey<Block> VARIABLE_MAGLEV_RAILS = tagOf("variable_maglev_rails");
    public static final TagKey<Block> HOVERABLE_RAILS = tagOf("hoverable_rails");
    public static final TagKey<Block> MANUALLY_OXIDIZABLE = tagOf("manually_oxidizable");

    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!player.getItemInHand(hand).is(MaglevItems.OXIDIZERS)) return InteractionResult.PASS;
            var pos = hitResult.getBlockPos();
            var state = world.getBlockState(pos);
            if (!state.is(MaglevBlocks.MANUALLY_OXIDIZABLE)) return InteractionResult.PASS;

            var oxidizedBlock = getNext(state.getBlock()).orElse(null);
            if (oxidizedBlock == null) return InteractionResult.FAIL;

            world.setBlockAndUpdate(pos, oxidizedBlock.withPropertiesOf(state));
            world.playSound(player, pos, SoundEvents.BOTTLE_EMPTY, player.getSoundSource());
            if (world instanceof ServerLevel serverWorld) {
                var particlePos = pos.getBottomCenter();
                serverWorld.sendParticles(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z, 8, 0.25, 0, 0.25, 0);
            }

            return InteractionResult.SUCCESS;
        });
    }
}
