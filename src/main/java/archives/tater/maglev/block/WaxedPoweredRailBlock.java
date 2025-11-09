package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.WeatheringCopper;

public class WaxedPoweredRailBlock extends PoweredRailBlock implements HasOxidationLevel, VariantPoweredRail {
    private final WeatheringCopper.WeatherState oxidationLevel;

    public WaxedPoweredRailBlock(WeatheringCopper.WeatherState oxidationLevel, Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return oxidationLevel;
    }
}
