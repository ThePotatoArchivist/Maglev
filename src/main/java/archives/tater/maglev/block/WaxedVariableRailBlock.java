package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.world.level.block.WeatheringCopper;

public class WaxedVariableRailBlock extends VariableRailBlock implements HasOxidationLevel, PolymerBlock {
    private final WeatheringCopper.WeatherState oxidationLevel;

    public WaxedVariableRailBlock(WeatheringCopper.WeatherState oxidationLevel, Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return oxidationLevel;
    }
}
