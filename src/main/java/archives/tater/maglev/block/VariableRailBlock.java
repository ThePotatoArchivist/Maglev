package archives.tater.maglev.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VariableRailBlock extends PoweredRailBlock {
//    public static final IntProperty POWER = Properties.POWER;

    public VariableRailBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean isPoweredByOtherRails(World world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        return false;
    }

    @Override
    protected boolean isPoweredByOtherRails(World world, BlockPos pos, BlockState state, boolean bl, int distance) {
        return false;
    }
}
