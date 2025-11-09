package archives.tater.maglev;

import net.minecraft.world.level.block.WeatheringCopper;

public interface HasOxidationLevel {
    WeatheringCopper.WeatherState getAge();
}
