package possibletriangle.skygrid.generator.custom;

import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateOptions {

    private final Set<Biome> biomes;

    public CreateOptions(Stream<Biome> biomes) {
        this.biomes = biomes.collect(Collectors.toSet());
    }

    public static CreateOptions merge(@Nullable CreateOptions a, @Nullable CreateOptions b) {
        if(a == null) return b;
        if(b == null) return a;

        Stream<Biome> biomes = Stream.of(a.biomes, b.biomes)
                .map(Collection::stream)
                .flatMap(Function.identity());

        return new CreateOptions(biomes);
    }

    public boolean isValid() {
        return !biomes.isEmpty();
    }

    public Set<Biome> getBiomes() {
        return this.biomes;
    }

}
