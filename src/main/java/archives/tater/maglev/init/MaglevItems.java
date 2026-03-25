package archives.tater.maglev.init;

import archives.tater.maglev.WeatheringCopperSetUtil;
import archives.tater.maglev.Maglev;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WeatheringCopperItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

import java.util.List;
import java.util.function.Function;

public class MaglevItems {

    private static Item register(Identifier id, Function<Item.Properties, Item> item, Item.Properties properties) {
        var key = ResourceKey.create(Registries.ITEM, id);
        return Registry.register(BuiltInRegistries.ITEM, key, item.apply(properties.setId(key)));
    }

    private static Item register(Block block) {
        return register(
                BuiltInRegistries.BLOCK.getKey(block),
                properties -> new BlockItem(block, properties),
                new Item.Properties().useBlockDescriptionPrefix()
        );
    }

    private static WeatheringCopperItems registerOxidizableItems(WeatheringCopperBlocks blockSet) {
        return WeatheringCopperItems.create(blockSet, MaglevItems::register);
    }

    private static TagKey<Item> tagOf(String path) {
        return TagKey.create(Registries.ITEM, Maglev.id(path));
    }

    public static final WeatheringCopperItems MAGLEV_RAIL = registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
    public static final WeatheringCopperItems POWERED_MAGLEV_RAIL = registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
    public static final WeatheringCopperItems VARIABLE_MAGLEV_RAIL = registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);

    private static final List<WeatheringCopperItems> itemSets = List.of(
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
                    .icon(() -> MAGLEV_RAIL.unaffected().getDefaultInstance())
                    .displayItems((_, output) -> output.acceptAll(
                            WeatheringCopperSetUtil.fields()
                                    .flatMap(field -> itemSets.stream().map(field))
                                    .map(Item::getDefaultInstance)
                                    .toList()
                    ))
                    .build()
    );

    public static final TagKey<Item> MAGLEV_RAILS = tagOf("maglev_rails");
    public static final TagKey<Item> POWERED_MAGLEV_RAILS = tagOf("powered_maglev_rails");
    public static final TagKey<Item> VARIABLE_MAGLEV_RAILS = tagOf("variable_maglev_rails");
    public static final TagKey<Item> HOVERABLE_RAILS = tagOf("hoverable_rails");
    public static final TagKey<Item> OXIDIZERS = tagOf("oxidizers");

    public static void init() {

    }
}
