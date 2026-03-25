package archives.tater.maglev.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class VariableRailBlock extends PoweredRailBlock {
//    public static final IntProperty POWER = Properties.POWER;

    public VariableRailBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean isSameRailWithPower(Level level, BlockPos pos, boolean bl, int distance, RailShape shape) {
        return false;
    }

    @Override
    protected boolean findPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean bl, int distance) {
        return false;
    }
}
