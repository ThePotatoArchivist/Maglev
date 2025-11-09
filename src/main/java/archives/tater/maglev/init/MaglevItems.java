package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks.CopperBlockSet;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import java.util.List;

/**
 * Use {@link Block#asItem()} to get the items from the {@link CopperBlockSet}s
 */
public class MaglevItems {

    private static TagKey<Item> tagOf(String path) {
        return TagKey.create(Registries.ITEM, Maglev.id(path));
    }

    private static final List<CopperBlockSet> blockSets = List.of(
            MaglevBlocks.MAGLEV_RAIL,
            MaglevBlocks.POWERED_MAGLEV_RAIL,
            MaglevBlocks.VARIABLE_MAGLEV_RAIL
    );

    public static final String ITEM_GROUP_NAME = "itemGroup.maglev.maglev";
    public static final CreativeModeTab ITEM_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Maglev.id("maglev_rails"),
            FabricItemGroup.builder()
                    .title(Component.translatable(ITEM_GROUP_NAME))
                    .icon(() -> MaglevBlocks.MAGLEV_RAIL.unaffected().asItem().getDefaultInstance())
                    .displayItems((displayContext, entries) -> entries.acceptAll(
                            CopperBlockSet.fields()
                                    .flatMap(field -> blockSets.stream().map(field))
                                    .map(Block::asItem)
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

    private static void registerOxidizableItems(CopperBlockSet blockSet) {
        for (var block : blockSet.getAll())
            Items.registerBlock(block);
    }

    public static void init() {
        registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
    }
}
