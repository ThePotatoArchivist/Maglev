package archives.tater.maglev.block;

import archives.tater.maglev.HasWeatherState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperRailBlock extends RailBlock implements WeatheringCopper, HasWeatherState {
    private final WeatherState oxidationLevel;

    public WeatheringCopperRailBlock(WeatherState oxidationLevel, Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return oxidationLevel;
    }
}
