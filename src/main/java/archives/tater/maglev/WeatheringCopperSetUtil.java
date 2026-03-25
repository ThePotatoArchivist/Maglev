package archives.tater.maglev;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.WeatheringCopperItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utilities to deal with {@link WeatheringCopperBlocks} and {@link WeatheringCopperItems}
 */
public class WeatheringCopperSetUtil {
    private WeatheringCopperSetUtil() {}

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

    public static Stream<Function<WeatheringCopperItems, Item>> fields() {
        return Stream.of(
                WeatheringCopperItems::unaffected,
                WeatheringCopperItems::exposed,
                WeatheringCopperItems::weathered,
                WeatheringCopperItems::oxidized,
                WeatheringCopperItems::waxed,
                WeatheringCopperItems::waxedExposed,
                WeatheringCopperItems::waxedWeathered,
                WeatheringCopperItems::waxedOxidized
        );
    }
}
