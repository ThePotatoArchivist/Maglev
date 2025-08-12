package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MaglevItems {
    private static final List<Item> items = new ArrayList<>();

    public static final ItemGroup MAGLEV_RAILS = Registry.register(
            Registries.ITEM_GROUP,
            Maglev.id("maglev_rails"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.maglev.maglev_rails"))
                    .icon(() -> MaglevBlocks.MAGLEV_RAIL.base().asItem().getDefaultStack())
                    .entries((displayContext, entries) -> {
                        entries.addAll(items.stream().map(Item::getDefaultStack).toList());
                    })
                    .build()
    );

    private static void registerOxidizableItems(MaglevBlocks.OxidizableBlockSet blockSet) {
        for (var block : blockSet)
            items.add(Items.register(block));
    }

    public static void init() {
        registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
    }
}
