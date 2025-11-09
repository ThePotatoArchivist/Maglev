package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import static archives.tater.maglev.init.MaglevDataAttachments.SPEED_MULTIPLIER;

public class OxidizablePoweredRailBlock extends PoweredRailBlock implements WeatheringCopper, HasOxidationLevel, VariantPoweredRail {
    private final WeatherState oxidationLevel;

    public OxidizablePoweredRailBlock(WeatherState oxidationLevel, Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        changeOverTime(state, world, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return oxidationLevel;
    }

    public static double getSpeedMultiplier(WeatherState level) {
        return switch (level) {
            case UNAFFECTED -> 3;
            case EXPOSED -> 2;
            case WEATHERED -> 1;
            case OXIDIZED -> 0.5;
        };
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void updateSpeed(AbstractMinecart minecart, BlockState state) {
        if (state.getBlock() instanceof HasOxidationLevel oxidizable)
            minecart.setAttached(SPEED_MULTIPLIER, OxidizablePoweredRailBlock.getSpeedMultiplier(oxidizable.getAge()));
        else
            minecart.removeAttached(SPEED_MULTIPLIER);
    }
}
