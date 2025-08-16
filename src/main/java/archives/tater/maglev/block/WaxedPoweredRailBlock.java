package archives.tater.maglev.block;

import archives.tater.maglev.HasOxidationLevel;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PoweredRailBlock;

public class WaxedPoweredRailBlock extends PoweredRailBlock implements HasOxidationLevel, VariantPoweredRail {
    private final Oxidizable.OxidationLevel oxidationLevel;

    public WaxedPoweredRailBlock(Oxidizable.OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public Oxidizable.OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }
}
