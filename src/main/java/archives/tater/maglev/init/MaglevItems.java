package archives.tater.maglev.init;

import archives.tater.maglev.CopperBlockSetUtil;
import archives.tater.maglev.HasOxidationLevel;
import archives.tater.maglev.Maglev;
import archives.tater.maglev.block.*;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.WeatheringCopperBlocks;
import org.jetbrains.annotations.NotNull;
import xyz.nucleoid.packettweaker.PacketContext;

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
            Items.registerBlock(block, (b, props) -> new PolymerBlockItem(b, props) {
                @Override
                public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
                    return block instanceof PoweredRailBlock ? Items.POWERED_RAIL : Items.RAIL;
                }

                @Override
                public @NotNull ResourceLocation getPolymerItemModel(ItemStack stack, PacketContext context) {
                    if (block instanceof HasOxidationLevel) {
                        String prefix = switch (((HasOxidationLevel) block).getAge()) {
                            case UNAFFECTED -> "";
                            case EXPOSED -> "exposed_";
                            case WEATHERED -> "weathered_";
                            case OXIDIZED -> "oxidized_";
                        };
                        if (block instanceof OxidizableRailBlock || block instanceof WaxedRailBlock) {
                            String preprefix = block instanceof WaxedRailBlock ? "waxed_" : "";
                            return Maglev.id(preprefix + prefix + "maglev_rail");
                        }
                        if (block instanceof OxidizablePoweredRailBlock || block instanceof WaxedPoweredRailBlock) {
                            String preprefix = block instanceof WaxedPoweredRailBlock ? "waxed_" : "";
                            return Maglev.id(preprefix + prefix + "powered_maglev_rail");
                        }
                        if (block instanceof OxidizableVariableRailBlock || block instanceof WaxedVariableRailBlock) {
                            String preprefix = block instanceof WaxedVariableRailBlock ? "waxed_" : "";
                            return Maglev.id(preprefix + prefix + "variable_maglev_rail");
                        }
                    }
                    return block instanceof PoweredRailBlock ? ResourceLocation.withDefaultNamespace("powered_rail") : ResourceLocation.withDefaultNamespace("rail");
                }
            });
    }

    public static void init() {
        registerOxidizableItems(MaglevBlocks.MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.POWERED_MAGLEV_RAIL);
        registerOxidizableItems(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
        PolymerItemGroupUtils.registerPolymerItemGroup(Maglev.id("maglev_rails"),
                PolymerItemGroupUtils.builder()
                        .title(Component.translatable(ITEM_GROUP_NAME))
                        .icon(() -> MaglevBlocks.MAGLEV_RAIL.unaffected().asItem().getDefaultInstance())
                        .displayItems((displayContext, entries) -> entries.acceptAll(
                                CopperBlockSetUtil.fields()
                                        .flatMap(field -> blockSets.stream().map(field))
                                        .map(Block::asItem)
                                        .map(Item::getDefaultInstance)
                                        .toList()
                        ))
                        .build());
    }
}
