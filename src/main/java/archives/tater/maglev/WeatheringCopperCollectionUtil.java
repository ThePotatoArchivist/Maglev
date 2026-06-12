package archives.tater.maglev;

import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utilities to deal with {@link WeatheringCopperCollection}
 */
public class WeatheringCopperCollectionUtil {
    private WeatheringCopperCollectionUtil() {}

    public static <T> boolean isOf(T value, WeatheringCopperCollection<T> collection) {
        return (value == collection.weathering().unaffected() ||
                value == collection.weathering().exposed() ||
                value == collection.weathering().weathered() ||
                value == collection.weathering().oxidized() ||
                value == collection.waxed().unaffected() ||
                value == collection.waxed().exposed() ||
                value == collection.waxed().weathered() ||
                value == collection.waxed().oxidized()
        );
    }

    private static <T> Stream<Function<WeatheringCopperCollection.ByState<T>, T>> byStateFields() {
        return Stream.of(
                WeatheringCopperCollection.ByState::unaffected,
                WeatheringCopperCollection.ByState::exposed,
                WeatheringCopperCollection.ByState::weathered,
                WeatheringCopperCollection.ByState::oxidized
        );
    }

    public static <T> Stream<Function<WeatheringCopperCollection<T>, T>> fields() {
        return Stream.concat(
                WeatheringCopperCollectionUtil.<T>byStateFields().map(field -> collection -> field.apply(collection.weathering())),
                WeatheringCopperCollectionUtil.<T>byStateFields().map(field -> collection -> field.apply(collection.waxed()))
        );
    }
}
