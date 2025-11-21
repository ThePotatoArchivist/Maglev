package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import archives.tater.maglev.Maglev;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import xyz.nucleoid.packettweaker.PacketContext;

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
    return OxidizablePoweredRailBlock.polymerMap.getOrDefault(new Maglev.RailState(blockState.getValue(SHAPE), blockState.getValue(POWERED)), Blocks.POWERED_RAIL.withPropertiesOf(blockState));
    }
}
