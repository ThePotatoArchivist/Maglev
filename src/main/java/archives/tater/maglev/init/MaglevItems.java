package archives.tater.maglev.init;

import archives.tater.maglev.CopperBlockSetUtil;
import archives.tater.maglev.Maglev;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
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
import net.minecraft.world.level.block.WeatheringCopperBlocks;
import java.util.List;

/**
 * Use {@link Block#asItem()} to get the items from the {@link WeatheringCopperBlocks}s
 */
public class MaglevItems {

    private static TagKey<Item> tagOf(String path) {
        return TagKey.create(Registries.ITEM, Maglev.id(path));
    }

    private static final List<WeatheringCopperBlocks> blockSets = List.of(
            MaglevBlocks.MAGLEV_RAIL,
            MaglevBlocks.POWERED_MAGLEV_RAIL,
            MaglevBlocks.VARIABLE_MAGLEV_RAIL
    );

    public static final String ITEM_GROUP_NAME = "itemGroup.maglev.maglev";

    public static final TagKey<Item> MAGLEV_RAILS = tagOf("maglev_rails");
    public static final TagKey<Item> POWERED_MAGLEV_RAILS = tagOf("powered_maglev_rails");
    public static final TagKey<Item> VARIABLE_MAGLEV_RAILS = tagOf("variable_maglev_rails");
    public static final TagKey<Item> HOVERABLE_RAILS = tagOf("hoverable_rails");
    public static final TagKey<Item> OXIDIZERS = tagOf("oxidizers");

    private static void registerOxidizableItems(WeatheringCopperBlocks blockSet) {
        for (var block : blockSet.asList())
            Items.register(block, (b, s) -> new PolymerBlockItem(b, s) {
                @Override
                public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
                    return block instanceof PoweredRailBlock ? Items.POWERED_RAIL : Items.RAIL;
                }

                @Override
                public @NotNull Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
                    return block instanceof PoweredRailBlock ? Identifier.ofVanilla("powered_rail") : Identifier.ofVanilla("rail");
                }
            });
    }

    public static void init() {
        registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        PolymerItemGroupUtils.registerPolymerItemGroup(Maglev.id("maglev_rails"),
                PolymerItemGroupUtils.builder()
                        .displayName(Text.translatable(ITEM_GROUP_NAME))
                        .icon(() -> MaglevBlocks.MAGLEV_RAIL.unaffected().asItem().getDefaultStack())
                        .entries((displayContext, entries) -> entries.addAll(
                                CopperBlockSetUtil.fields()
                                        .flatMap(field -> blockSets.stream().map(field))
                                        .map(Block::asItem)
                                        .map(Item::getDefaultStack)
                                        .toList()
                        ))
                        .build());
    }
}
