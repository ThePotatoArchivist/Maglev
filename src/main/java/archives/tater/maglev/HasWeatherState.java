package archives.tater.maglev;

import net.minecraft.world.level.block.WeatheringCopper;

public interface HasWeatherState {
    WeatheringCopper.WeatherState getAge();
}
