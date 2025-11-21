package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import archives.tater.maglev.Maglev;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;

public class OxidizableRailBlock extends RailBlock implements WeatheringCopper, HasOxidationLevel, PolymerTexturedBlock {
    private final WeatherState oxidationLevel;

    public static final HashMap<RailShape, BlockState> polymerMap = new HashMap<>() {{
        for (RailShape shape : RailShape.values()) {
            String suffix = switch (shape) {
                case NORTH_SOUTH, EAST_WEST -> "";
                case ASCENDING_EAST, ASCENDING_NORTH -> "_raised_ne";
                case ASCENDING_WEST, ASCENDING_SOUTH -> "_raised_se";
                case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST -> "_corner";
            };
            int rotation = switch (shape) {
                case NORTH_SOUTH, SOUTH_EAST, ASCENDING_NORTH, ASCENDING_SOUTH -> 0;
                case EAST_WEST, ASCENDING_EAST, ASCENDING_WEST, SOUTH_WEST -> 90;
                case NORTH_WEST -> 180;
                case NORTH_EAST -> 270;
            };
            put(shape, PolymerBlockResourceUtils.requestBlock(shape.isSlope() ? BlockModelType.TRIPWIRE_BLOCK : BlockModelType.TRIPWIRE_BLOCK_FLAT, PolymerBlockModel.of(Maglev.id("block/maglev_rail" + suffix), 0, rotation)));
        }
    }};

    public OxidizableRailBlock(WeatherState oxidationLevel, Properties settings) {
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

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return polymerMap.getOrDefault(blockState.getValue(SHAPE), Blocks.RAIL.withPropertiesOf(blockState));
    }
}
