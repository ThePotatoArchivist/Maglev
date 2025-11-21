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
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;

import static archives.tater.maglev.init.MaglevDataAttachments.SPEED_MULTIPLIER;

public class OxidizablePoweredRailBlock extends PoweredRailBlock implements WeatheringCopper, HasOxidationLevel, VariantPoweredRail, PolymerTexturedBlock {
    private final WeatherState oxidationLevel;

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
            put(new Maglev.RailState(shape, false), PolymerBlockResourceUtils.requestBlock(shape.isSlope() ? BlockModelType.TRIPWIRE_BLOCK : BlockModelType.TRIPWIRE_BLOCK_FLAT, PolymerBlockModel.of(Maglev.id("block/powered_maglev_rail" + suffix), 0, rotation)));
            put(new Maglev.RailState(shape, true), PolymerBlockResourceUtils.requestBlock(shape.isSlope() ? BlockModelType.TRIPWIRE_BLOCK : BlockModelType.TRIPWIRE_BLOCK_FLAT, PolymerBlockModel.of(Maglev.id("block/powered_maglev_rail_on" + suffix.replaceAll("_se", "_sw")), 0, rotation)));
        }
    }};

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

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return polymerMap.getOrDefault(new Maglev.RailState(blockState.getValue(SHAPE), blockState.getValue(POWERED)), Blocks.POWERED_RAIL.withPropertiesOf(blockState));
    }
}
