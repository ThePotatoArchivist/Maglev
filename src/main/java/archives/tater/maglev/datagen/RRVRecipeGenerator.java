package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class RRVRecipeGenerator extends FabricCodecDataProvider<RRVRecipeGenerator.WorldInteractionRecipe> {

    public RRVRecipeGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "rrv_world_interaction", WorldInteractionRecipe.CODEC);
    }

    public record WorldInteractionRecipe(Ingredient left, Ingredient right, Ingredient result) {

        private WorldInteractionRecipe(Object ignored, Ingredient left, Ingredient right, Ingredient result) {
            this(left, right, result);
        }

        public static final Codec<WorldInteractionRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("type").forGetter(ignored -> "rrv:world_interaction"),
                Ingredient.CODEC.fieldOf("left").forGetter(WorldInteractionRecipe::left),
                Ingredient.CODEC.fieldOf("right").forGetter(WorldInteractionRecipe::right),
                Ingredient.CODEC.fieldOf("result").forGetter(WorldInteractionRecipe::result)
        ).apply(instance, WorldInteractionRecipe::new));

    }

    private static Pair<Identifier, WorldInteractionRecipe> createOxidizingRecipe(Block before, Ingredient oxidizer, Block after) {
        return Pair.of(
                BuiltInRegistries.BLOCK.getKey(after),
                new WorldInteractionRecipe(Ingredient.of(before), oxidizer, Ingredient.of(after))
        );
    }

    private static Stream<Pair<Identifier, WorldInteractionRecipe>> createOxidizingRecipes(WeatheringCopperBlocks blocks, Ingredient oxidizer) {
        return Stream.of(
                createOxidizingRecipe(blocks.unaffected(), oxidizer, blocks.exposed()),
                createOxidizingRecipe(blocks.exposed(), oxidizer, blocks.weathered()),
                createOxidizingRecipe(blocks.weathered(), oxidizer, blocks.oxidized())
        );
    }

    @Override
    protected void configure(BiConsumer<Identifier, WorldInteractionRecipe> provider, HolderLookup.Provider lookup) {
        var oxidizers = Ingredient.of(lookup.lookupOrThrow(Registries.ITEM).getOrThrow(MaglevItems.OXIDIZERS));

        Stream.of(MaglevBlocks.MAGLEV_RAIL, MaglevBlocks.POWERED_MAGLEV_RAIL, MaglevBlocks.VARIABLE_MAGLEV_RAIL)
                .flatMap((WeatheringCopperBlocks blocks) -> createOxidizingRecipes(blocks, oxidizers))
                .forEach(pair -> provider.accept(pair.getFirst(), pair.getSecond()));
    }

    @Override
    public String getName() {
        return "RRV Recipes";
    }
}
