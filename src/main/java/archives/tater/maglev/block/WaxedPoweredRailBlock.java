package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.WeatheringCopper;

public class WaxedPoweredRailBlock extends PoweredRailBlock implements HasOxidationLevel, VariantPoweredRail, PolymerBlock {
    private final WeatheringCopper.WeatherState oxidationLevel;

    public WaxedPoweredRailBlock(WeatheringCopper.WeatherState oxidationLevel, Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return oxidationLevel;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.POWERED_RAIL.getStateWithProperties(blockState);
    }
}
