package archives.tater.maglev;

import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

/**
 * Utilities to deal with {@link WeatheringCopperBlocks}
 */
public class CopperBlockSetUtil {
    private CopperBlockSetUtil() {}

    public static boolean isOf(Block block, WeatheringCopperBlocks blockSet) {
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

    public static Stream<Function<WeatheringCopperBlocks, Block>> fields() {
        return Stream.of(
                WeatheringCopperBlocks::unaffected,
                WeatheringCopperBlocks::exposed,
                WeatheringCopperBlocks::weathered,
                WeatheringCopperBlocks::oxidized,
                WeatheringCopperBlocks::waxed,
                WeatheringCopperBlocks::waxedExposed,
                WeatheringCopperBlocks::waxedWeathered,
                WeatheringCopperBlocks::waxedOxidized
        );
    }
}
