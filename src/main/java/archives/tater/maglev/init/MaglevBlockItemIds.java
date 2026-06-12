package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;

import net.minecraft.references.BlockItemId;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public class MaglevBlockItemIds {

    private static WeatheringCopperCollection<BlockItemId> createSimpleCopper(final String baseName) {
        return WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.create(baseName)).map(name -> BlockItemId.create(Maglev.id(name), Maglev.id(name)));
    }

    public static final WeatheringCopperCollection<BlockItemId> MAGLEV_RAIL = createSimpleCopper("maglev_rail");
    public static final WeatheringCopperCollection<BlockItemId> POWERED_MAGLEV_RAIL = createSimpleCopper("powered_maglev_rail");
    public static final WeatheringCopperCollection<BlockItemId> VARIABLE_MAGLEV_RAIL = createSimpleCopper("variable_maglev_rail");
}
