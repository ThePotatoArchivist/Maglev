package archives.tater.maglev;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import static archives.tater.maglev.init.MaglevDataAttachments.HOVER_SPEED;

public class OxidizablePoweredRailBlock extends PoweredRailBlock implements Oxidizable, HasOxidationLevel {
    private final OxidationLevel oxidationLevel;

    public OxidizablePoweredRailBlock(OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        tickDegradation(state, world, pos, random);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }

    public static double getSpeed(OxidationLevel level) {
        return switch (level) {
            case UNAFFECTED -> 1.2;
            case EXPOSED -> 0.8;
            case WEATHERED -> 0.4;
            case OXIDIZED -> 0.1;
        };
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void updateSpeed(AbstractMinecartEntity minecart, BlockState state) {
        if (state.getBlock() instanceof HasOxidationLevel oxidizable)
            minecart.setAttached(HOVER_SPEED, OxidizablePoweredRailBlock.getSpeed(oxidizable.getDegradationLevel()));
        else
            minecart.removeAttached(HOVER_SPEED);
    }
}
