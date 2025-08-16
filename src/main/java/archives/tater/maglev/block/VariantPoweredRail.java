package archives.tater.maglev.block;

public interface VariantPoweredRail {
    default VariantRailType getVariantRailType() {
        return POWERED_RAIL;
    }

    interface VariantRailType {}

    VariantRailType POWERED_RAIL = new VariantRailType() {};
}
