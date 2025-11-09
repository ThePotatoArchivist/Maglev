package archives.tater.maglev.datagen;

import archives.tater.maglev.Maglev;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static archives.tater.maglev.datagen.DatagenUtil.*;
import static archives.tater.maglev.datagen.DatagenUtil.read;

public final class TextureGenerator implements DataProvider {
    public static final String EMISSIVE_SUFFIX = "_e";

    private final FabricDataOutput dataOutput;
    private final CompletableFuture<Provider> registryLookup;
    private final PathProvider pathResolver;

    public TextureGenerator(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
        pathResolver = dataOutput.createPathProvider(Target.RESOURCE_PACK, "textures");
    }

    private static ResourceLocation toEmissiveBlockId(String name) {
        return Maglev.id("block/" + name + EMISSIVE_SUFFIX);
    }

    private Path toEmissiveBlockPath(String name) {
        return pathResolver.file(toEmissiveBlockId(name), "png");
    }

    private CompletableFuture<Void> writeEmissives(CachedOutput writer, byte[] data, Stream<String> names) {
        return writeAll(writer, data, names.map(this::toEmissiveBlockPath));
    }

    private CompletableFuture<Void> copyEmissives(CachedOutput writer, Path source, Stream<String> names) {
        return read(source).thenComposeAsync(data ->
                writeEmissives(writer, data, names)
        ).exceptionally(throwable -> {
            Maglev.LOGGER.error("Datagen:", throwable);
            return null;
        });
    }

    public static final List<UnaryOperator<String>> OXIDATIONS = prefixes("", "exposed_", "weathered_", "oxidized_");
    public static final List<UnaryOperator<String>> DEFAULT_TYPES = prefixes("", "powered_");

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        var blockTextures = Path.of(System.getProperty("user.dir")) // build/datagen
                .getParent().getParent() // Project root
                .resolve("src/main/resources/assets/maglev/textures/block")
                .toAbsolutePath();

        return CompletableFuture.allOf(
                copyEmissives(writer, blockTextures.resolve("maglev_rail_glow.png"), Stream.of("maglev_rail")
                        .flatMap(multiply(DEFAULT_TYPES))
                        .flatMap(multiply(OXIDATIONS))
                ),
                copyEmissives(writer, blockTextures.resolve("powered_maglev_rail_glow.png"), Stream.of("powered_maglev_rail_on")
                        .flatMap(multiply(OXIDATIONS))
                ),
                copyEmissives(writer, blockTextures.resolve("maglev_rail_glow_corner.png"), Stream.of("maglev_rail_corner")
                        .flatMap(multiply(OXIDATIONS))
                ),
                copyEmissives(writer, blockTextures.resolve("variable_maglev_rail_glow.png"), Stream.of("variable_maglev_rail_on")
                        .flatMap(multiply(OXIDATIONS))
                )
        );
    }

    @Override
    public String getName() {
        return "Textures";
    }
}
