package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import static archives.tater.maglev.init.MaglevDataAttachments.SPEED_MULTIPLIER;

public class OxidizablePoweredRailBlock extends PoweredRailBlock implements Oxidizable, HasOxidationLevel, VariantPoweredRail {
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

    public static double getSpeedMultiplier(OxidationLevel level) {
        return switch (level) {
            case UNAFFECTED -> 3;
            case EXPOSED -> 2;
            case WEATHERED -> 1;
            case OXIDIZED -> 0.5;
        };
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void updateSpeed(AbstractMinecartEntity minecart, BlockState state) {
        if (state.getBlock() instanceof HasOxidationLevel oxidizable)
            minecart.setAttached(SPEED_MULTIPLIER, OxidizablePoweredRailBlock.getSpeedMultiplier(oxidizable.getDegradationLevel()));
        else
            minecart.removeAttached(SPEED_MULTIPLIER);
    }
}
