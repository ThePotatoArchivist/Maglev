package archives.tater.maglev;

import net.minecraft.block.Oxidizable;

public class WaxedVariableRailBlock extends VariableRailBlock implements HasOxidationLevel {
    private final Oxidizable.OxidationLevel oxidationLevel;

    public WaxedVariableRailBlock(Oxidizable.OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public Oxidizable.OxidationLevel getDegradationLevel() {
        return oxidationLevel;
    }
}
