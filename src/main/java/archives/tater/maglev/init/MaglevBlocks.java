package archives.tater.maglev.init;

import archives.tater.maglev.*;
import net.minecraft.block.*;
import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class MaglevBlocks {
    private static Block register(String path, Function<AbstractBlock.Settings, Block> constructor, AbstractBlock.Settings settings) {
        return Blocks.register(RegistryKey.of(RegistryKeys.BLOCK, Maglev.id(path)), constructor, settings);
    }

    public record OxidizableBlockSet(
            Block base,
            Block exposed,
            Block weathered,
            Block oxidized,
            Block waxedBase,
            Block waxedExposed,
            Block waxedWeathered,
            Block waxedOxidized
    ) implements Iterable<Block> {
        public Stream<Block> stream() {
            return Stream.of(base, exposed, weathered, oxidized, waxedBase, waxedExposed, waxedWeathered, waxedOxidized);
        }

        @Override
        public @NotNull Iterator<Block> iterator() {
            return stream().iterator();
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

    private static Block registerWaxedRail(String name, OxidationLevel oxidationLevel, Function<AbstractBlock.Settings, Block> constructor) {
        return register(
                "waxed_" + getOxidizedName(name, oxidationLevel),
                constructor,
                AbstractBlock.Settings.copy(Blocks.RAIL)
        );
    }

    private static OxidizableBlockSet registerOxidizableRails(String name, Function<AbstractBlock.Settings, Block> waxedConstructor, BiFunction<OxidationLevel, AbstractBlock.Settings, Block> oxidizableConstructor) {
        return new OxidizableBlockSet(
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

    public static final OxidizableBlockSet MAGLEV_RAIL = registerOxidizableRails("maglev_rail", RailBlock::new, OxidizableRailBlock::new);
    public static final OxidizableBlockSet VARIABLE_MAGLEV_RAIL = registerOxidizableRails("variable_maglev_rail", VariableRailBlock::new, OxidizableVariableRailBlock::new);
    public static final OxidizableBlockSet POWERED_MAGLEV_RAIL = registerOxidizableRails("powered_maglev_rail", PoweredRailBlock::new, OxidizablePoweredRailBlock::new);

    public static final TagKey<Block> MAGLEV_RAILS = TagKey.of(RegistryKeys.BLOCK, Maglev.id("maglev_rails"));

    public static void init() {

    }
}
