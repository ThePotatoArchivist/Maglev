package archives.tater.maglev.block;

import archives.tater.maglev.Maglev;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;

public class VariableRailBlock extends PoweredRailBlock implements PolymerTexturedBlock {
//    public static final IntProperty POWER = Properties.POWER;

    public static final HashMap<Maglev.RailState, BlockState> polymerMap = new HashMap<>() {{
        for (RailShape shape : RailShape.values()) {
            if (switch (shape) {
                case NORTH_SOUTH, EAST_WEST, ASCENDING_EAST, ASCENDING_WEST, ASCENDING_NORTH, ASCENDING_SOUTH -> false;
                case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST -> true;
            }) {
                continue;
            }
            String suffix = switch (shape) {
                case NORTH_SOUTH, EAST_WEST -> "";
                case ASCENDING_EAST, ASCENDING_NORTH -> "_raised_ne";
                case ASCENDING_WEST, ASCENDING_SOUTH -> "_raised_se";
                default -> throw new IllegalStateException("Unexpected value: " + shape);
            };
            int rotation = switch (shape) {
                case NORTH_SOUTH, ASCENDING_NORTH, ASCENDING_SOUTH -> 0;
                case EAST_WEST, ASCENDING_EAST, ASCENDING_WEST -> 90;
                default -> throw new IllegalStateException("Unexpected value: " + shape);
            };
            put(new Maglev.RailState(shape, false), PolymerBlockResourceUtils.requestBlock(shape.isSlope() ? BlockModelType.TRIPWIRE_BLOCK : BlockModelType.TRIPWIRE_BLOCK_FLAT, PolymerBlockModel.of(Maglev.id("block/variable_maglev_rail" + suffix.replaceAll("_se", "_sw")), 0, rotation)));
            put(new Maglev.RailState(shape, true), PolymerBlockResourceUtils.requestBlock(shape.isSlope() ? BlockModelType.TRIPWIRE_BLOCK : BlockModelType.TRIPWIRE_BLOCK_FLAT, PolymerBlockModel.of(Maglev.id("block/variable_maglev_rail_on" + suffix.replaceAll("_se", "_sw")), 0, rotation)));
        }
    }};

    public VariableRailBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean isSameRailWithPower(Level world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        return false;
    }

    @Override
    protected boolean findPoweredRailSignal(Level world, BlockPos pos, BlockState state, boolean bl, int distance) {
        return false;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return polymerMap.getOrDefault(new Maglev.RailState(blockState.getValue(SHAPE), blockState.getValue(POWERED)), Blocks.POWERED_RAIL.withPropertiesOf(blockState));
    }
}
