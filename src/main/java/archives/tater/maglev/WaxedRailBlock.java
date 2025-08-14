package archives.tater.maglev;

import net.minecraft.block.Oxidizable;
import net.minecraft.block.RailBlock;

public class WaxedRailBlock extends RailBlock implements HasOxidationLevel {
    private final Oxidizable.OxidationLevel oxidationLevel;

    public WaxedRailBlock(Oxidizable.OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public Oxidizable.OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }
}
