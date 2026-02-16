package archives.tater.maglev.emi;

import archives.tater.maglev.Maglev;
import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;

import net.minecraft.core.registries.BuiltInRegistries;
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
    private static String getPath(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private static EmiWorldInteractionRecipe createOxidizingRecipe(Block before, EmiIngredient oxidizers, Block after) {
        return EmiWorldInteractionRecipe.builder()
                .id(Maglev.id("/manual_oxidation/" + getPath(after)))
                .leftInput(EmiStack.of(before))
                .rightInput(oxidizers, true)
                .output(EmiStack.of(after))
                .build();
    }

    private static Stream<EmiWorldInteractionRecipe> createOxidizingRecipes(MaglevBlocks.WeatheringCopperBlocks blocks, EmiIngredient oxidizers) {
        return Stream.of(
                createOxidizingRecipe(blocks.unaffected(), oxidizers, blocks.exposed()),
                createOxidizingRecipe(blocks.exposed(), oxidizers, blocks.weathered()),
                createOxidizingRecipe(blocks.weathered(), oxidizers, blocks.oxidized())
        );
    }

    @Override
    public void register(EmiRegistry emiRegistry) {
        var oxidizers = EmiIngredient.of(MaglevItems.OXIDIZERS);

        Stream.of(MaglevBlocks.MAGLEV_RAIL, MaglevBlocks.POWERED_MAGLEV_RAIL, MaglevBlocks.VARIABLE_MAGLEV_RAIL)
                .flatMap(blockSet -> createOxidizingRecipes(blockSet, oxidizers))
                .forEach(emiRegistry::addRecipe);
    }
}
