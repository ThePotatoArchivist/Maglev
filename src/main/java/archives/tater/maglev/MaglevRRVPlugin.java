package archives.tater.maglev;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.WeatheringCopper;

import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.common.builtin.interaction.WorldInteractionClientRecipe;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;

public class MaglevRRVPlugin implements ReliableRecipeViewerClientPlugin {
    @Override
    public void onIntegrationInitialize() {
        ItemView.addClientReloadCallback(() -> {
            var oxidizer = SlotContent.of(MaglevItems.OXIDIZERS);

            for (var holder : BuiltInRegistries.BLOCK.getOrThrow(MaglevBlocks.MANUALLY_OXIDIZABLE)) {
                var unoxidized = holder.value();
                var oxidized = WeatheringCopper.getNext(unoxidized).orElse(null);
                if (oxidized == null) continue;
                ItemView.addWorldInteractionRecipe(new WorldInteractionClientRecipe(
                        SlotContent.of(unoxidized),
                        oxidizer,
                        SlotContent.of(oxidized)
                ));
            }
        });
    }
}
