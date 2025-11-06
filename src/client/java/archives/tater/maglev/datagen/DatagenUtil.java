package archives.tater.maglev.datagen;

import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Util;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Stream;

public class DatagenUtil {
    private DatagenUtil() {
    }

    public static CompletableFuture<byte[]> read(Path path) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    private static CompletableFuture<Void> write(DataWriter writer, byte[] data, Path path) {
        return CompletableFuture.runAsync(() -> {
            try {
                var byteStream = new ByteArrayOutputStream();
                var hashingStream = new HashingOutputStream(Hashing.sha1(), byteStream);
                hashingStream.write(data);
                writer.write(path, byteStream.toByteArray(), hashingStream.hash());
            } catch (IOException exception) {
                DataProvider.LOGGER.error("Failed to save file to {}", path, exception);
            }
        }, Util.getMainWorkerExecutor());
    }

    static CompletableFuture<Void> writeAll(DataWriter writer, byte[] data, Stream<Path> paths) {
        return paths.map(path -> write(writer, data, path)).collect(futureAllOf());
    }

    /**
     * Used for {@link Stream#flatMap}
     */
    static <T> Function<T, Stream<T>> multiply(Collection<UnaryOperator<T>> operators) {
        return value -> operators.stream().map(operator -> operator.apply(value));
    }

    static List<UnaryOperator<String>> prefixes(String... prefixes) {
        return Stream.of(prefixes)
                .<UnaryOperator<String>>map(prefix -> value -> prefix + value)
                .toList();
    }

    private static List<UnaryOperator<String>> suffixes(String... suffixes) {
        return Stream.of(suffixes)
                .<UnaryOperator<String>>map(suffix -> value -> value + suffix)
                .toList();
    }

    public static Collector<CompletableFuture<?>, ?, CompletableFuture<Void>> futureAllOf() {
        return Collector.<CompletableFuture<?>, List<CompletableFuture<?>>, CompletableFuture<Void>>of(ArrayList::new, List::add, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }, futures ->
                        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                , Characteristics.UNORDERED);
    }
}
