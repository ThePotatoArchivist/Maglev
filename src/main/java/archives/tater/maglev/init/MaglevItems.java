package archives.tater.maglev.init;

import net.minecraft.item.Items;

public class MaglevItems {
    private static void registerOxidizableItems(MaglevBlocks.OxidizableBlockSet blockSet) {
        for (var block : blockSet)
            Items.register(block);
    }

    public static void init() {
        registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
    }
}
