package archives.tater.maglev.emi;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import java.util.stream.Stream;

@EmiEntrypoint
public class MaglevEmiPlugin implements EmiPlugin {
    private static final EmiIngredient WET_SPONGE = EmiStack.of(Items.WET_SPONGE);

    private static String getPath(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private static EmiWorldInteractionRecipe createOxidizingRecipe(Block before, Block after) {
        return EmiWorldInteractionRecipe.builder()
                .id(Maglev.id("/manual_oxidation/" + getPath(after)))
                .leftInput(EmiStack.of(before))
                .rightInput(WET_SPONGE, true)
                .output(EmiStack.of(after))
                .build();
    }

    private static Stream<EmiWorldInteractionRecipe> createOxidizingRecipes(MaglevBlocks.CopperBlockSet blockSet) {
        return Stream.of(
                createOxidizingRecipe(blockSet.unaffected(), blockSet.exposed()),
                createOxidizingRecipe(blockSet.exposed(), blockSet.weathered()),
                createOxidizingRecipe(blockSet.weathered(), blockSet.oxidized())
        );
    }

    @Override
    public void register(EmiRegistry emiRegistry) {
        Stream.of(MaglevBlocks.MAGLEV_RAIL, MaglevBlocks.POWERED_MAGLEV_RAIL, MaglevBlocks.VARIABLE_MAGLEV_RAIL)
                .flatMap(MaglevEmiPlugin::createOxidizingRecipes)
                .forEach(emiRegistry::addRecipe);
    }
}
