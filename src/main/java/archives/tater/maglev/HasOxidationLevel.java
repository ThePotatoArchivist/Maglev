package archives.tater.maglev;

import net.minecraft.block.Oxidizable;

public interface HasOxidationLevel {
    Oxidizable.OxidationLevel getDegradationLevel();
}
