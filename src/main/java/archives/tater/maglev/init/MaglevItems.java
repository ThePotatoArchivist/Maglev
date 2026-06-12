package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.WeatheringCopperCollectionUtil;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.List;
import java.util.function.Function;

public class MaglevItems {

    private static Item register(ResourceKey <Item> key, Function<Item.Properties, Item> item, Item.Properties properties) {
        return Registry.register(BuiltInRegistries.ITEM, key, item.apply(properties.setId(key)));
    }

    private static Item register(BlockItemId id, Block block) {
        return register(
                id.item(),
                properties -> new BlockItem(block, properties),
                new Item.Properties().useBlockDescriptionPrefix()
        );
    }

    private static WeatheringCopperCollection<Item> registerOxidizableItems(WeatheringCopperCollection<BlockItemId> ids, WeatheringCopperCollection<Block> blockSet) {
        return WeatheringCopperCollection.registerItems(ids, blockSet, MaglevItems::register);
    }

    private static TagKey<Item> tagOf(String path) {
        return TagKey.create(Registries.ITEM, Maglev.id(path));
    }

    public static final WeatheringCopperCollection<Item> MAGLEV_RAIL = registerOxidizableItems(MaglevBlockItemIds.MAGLEV_RAIL, MaglevBlocks.MAGLEV_RAIL);
    public static final WeatheringCopperCollection<Item> POWERED_MAGLEV_RAIL = registerOxidizableItems(MaglevBlockItemIds.POWERED_MAGLEV_RAIL, MaglevBlocks.POWERED_MAGLEV_RAIL);
    public static final WeatheringCopperCollection<Item> VARIABLE_MAGLEV_RAIL = registerOxidizableItems(MaglevBlockItemIds.VARIABLE_MAGLEV_RAIL, MaglevBlocks.VARIABLE_MAGLEV_RAIL);

    private static final List<WeatheringCopperCollection<Item>> itemSets = List.of(
            MAGLEV_RAIL,
            POWERED_MAGLEV_RAIL,
            VARIABLE_MAGLEV_RAIL
    );

    public static final String ITEM_GROUP_NAME = "itemGroup.maglev.maglev";
    public static final CreativeModeTab ITEM_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Maglev.id("maglev_rails"),
            FabricCreativeModeTab.builder()
                    .title(Component.translatable(ITEM_GROUP_NAME))
                    .icon(() -> MAGLEV_RAIL.weathering().unaffected().getDefaultInstance())
                    .displayItems((_, output) -> output.acceptAll(
                            WeatheringCopperCollectionUtil.<Item>fields()
                                    .flatMap(field -> itemSets.stream().map(field))
                                    .map(Item::getDefaultInstance)
                                    .toList()
                    ))
                    .build()
    );

    public static final TagKey<Item> OXIDIZERS = tagOf("oxidizers");

    public static void init() {

    }
}
