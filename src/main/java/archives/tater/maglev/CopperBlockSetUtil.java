package archives.tater.maglev;

import net.minecraft.block.Block;
import net.minecraft.block.CopperBlockSet;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utilities to deal with {@link CopperBlockSet}
 */
public class CopperBlockSetUtil {
    private CopperBlockSetUtil() {}

    public static boolean isOf(Block block, CopperBlockSet blockSet) {
        return (block == blockSet.unaffected() ||
                block == blockSet.exposed() ||
                block == blockSet.weathered() ||
                block == blockSet.oxidized() ||
                block == blockSet.waxed() ||
                block == blockSet.waxedExposed() ||
                block == blockSet.waxedWeathered() ||
                block == blockSet.waxedOxidized()
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
