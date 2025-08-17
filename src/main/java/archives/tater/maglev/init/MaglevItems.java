package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Use {@link Block#asItem()} to get the items from the {@link archives.tater.maglev.init.MaglevBlocks.OxidizableBlockSet}s
 */
public class MaglevItems {

    private static final List<MaglevBlocks.OxidizableBlockSet> blockSets = List.of(
            MaglevBlocks.MAGLEV_RAIL,
            MaglevBlocks.POWERED_MAGLEV_RAIL,
            MaglevBlocks.VARIABLE_MAGLEV_RAIL
    );

    public static final String ITEM_GROUP_NAME = "itemGroup.maglev.maglev";
    public static final ItemGroup ITEM_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Maglev.id("maglev_rails"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(ITEM_GROUP_NAME))
                    .icon(() -> MaglevBlocks.MAGLEV_RAIL.base().asItem().getDefaultStack())
                    .entries((displayContext, entries) -> {
                        entries.addAll(MaglevBlocks.OxidizableBlockSet.fields()
                                .flatMap(field -> blockSets.stream().map(field))
                                .map(Block::asItem)
                                .map(Item::getDefaultStack).toList());
                    })
                    .build()
    );

    private static void registerOxidizableItems(MaglevBlocks.OxidizableBlockSet blockSet) {
        for (var block : blockSet)
            Items.register(block);
    }

    public static void init() {
        registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
    }
}
