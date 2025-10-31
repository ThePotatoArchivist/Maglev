package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.block.*;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static net.minecraft.block.Oxidizable.getIncreasedOxidationBlock;

public class MaglevBlocks {
    private static Block register(String path, Function<AbstractBlock.Settings, Block> constructor, AbstractBlock.Settings settings) {
        return Blocks.register(RegistryKey.of(RegistryKeys.BLOCK, Maglev.id(path)), constructor.apply(settings));
    }

    private static TagKey<Block> tagOf(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Maglev.id(path));
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

    private static String getOxidizedName(String name, OxidationLevel oxidationLevel) {
        return oxidationLevel == OxidationLevel.UNAFFECTED ? name : oxidationLevel.asString() + "_" + name;
    }

    private static Block registerOxidizableRail(String name, OxidationLevel oxidationLevel, BiFunction<OxidationLevel, AbstractBlock.Settings, Block> constructor) {
        return register(
                getOxidizedName(name, oxidationLevel),
                settings -> constructor.apply(oxidationLevel, settings),
                AbstractBlock.Settings.copy(Blocks.RAIL)
        );
    }

    private static Block registerWaxedRail(String name, OxidationLevel oxidationLevel, BiFunction<OxidationLevel, AbstractBlock.Settings, Block> constructor) {
        return register(
                "waxed_" + getOxidizedName(name, oxidationLevel),
                settings -> constructor.apply(oxidationLevel, settings),
                AbstractBlock.Settings.copy(Blocks.RAIL)
        );
    }

    private static CopperBlockSet registerOxidizableRails(String name, BiFunction<OxidationLevel, AbstractBlock.Settings, Block> waxedConstructor, BiFunction<OxidationLevel, AbstractBlock.Settings, Block> oxidizableConstructor) {
        return new CopperBlockSet(
                registerOxidizableRail(name, OxidationLevel.UNAFFECTED, oxidizableConstructor),
                registerOxidizableRail(name, OxidationLevel.EXPOSED, oxidizableConstructor),
                registerOxidizableRail(name, OxidationLevel.WEATHERED, oxidizableConstructor),
                registerOxidizableRail(name, OxidationLevel.OXIDIZED, oxidizableConstructor),
                registerWaxedRail(name, OxidationLevel.UNAFFECTED, waxedConstructor),
                registerWaxedRail(name, OxidationLevel.EXPOSED, waxedConstructor),
                registerWaxedRail(name, OxidationLevel.WEATHERED, waxedConstructor),
                registerWaxedRail(name, OxidationLevel.OXIDIZED, waxedConstructor)
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
            if (!player.getStackInHand(hand).isIn(MaglevItems.OXIDIZERS)) return ActionResult.PASS;
            var pos = hitResult.getBlockPos();
            var state = world.getBlockState(pos);
            if (!state.isIn(MaglevBlocks.MANUALLY_OXIDIZABLE)) return ActionResult.PASS;

            var oxidizedBlock = getIncreasedOxidationBlock(state.getBlock()).orElse(null);
            if (oxidizedBlock == null) return ActionResult.FAIL;

            world.setBlockState(pos, oxidizedBlock.getStateWithProperties(state));
            world.playSound(player, pos, SoundEvents.ITEM_BOTTLE_EMPTY, player.getSoundCategory());
            if (world instanceof ServerWorld serverWorld) {
                var particlePos = pos.toBottomCenterPos();
                serverWorld.spawnParticles(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z, 8, 0.25, 0, 0.25, 0);
            }

            return ActionResult.SUCCESS;
        });
    }
}
