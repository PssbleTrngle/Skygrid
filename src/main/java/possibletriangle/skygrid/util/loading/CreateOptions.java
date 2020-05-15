package possibletriangle.skygrid.util.loading;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateOptions {

    private final Set<Biome> biomes;
    public final boolean enable;
    public final String daytime;

    @Nullable
    private final Vec3d fogColor, skyColor;

    public final boolean canRespawn, skyLight, waterVaporize;

    public CreateOptions(Stream<Biome> biomes, boolean canRespawn, boolean skyLight, boolean waterVaporize, @Nullable Vec3d fogColor, @Nullable Vec3d skyColor, String daytime, boolean enable) {
        this.biomes = biomes.collect(Collectors.toSet());
        this.fogColor = fogColor;
        this.skyColor = skyColor;
        this.canRespawn = canRespawn;
        this.skyLight = skyLight;
        this.waterVaporize = waterVaporize;
        this.daytime = daytime;
        this.enable = enable;
    }

    public static CreateOptions merge(@Nullable CreateOptions a, @Nullable CreateOptions b) {
        if(a == null) return b;
        if(b == null) return a;

        Stream<Biome> biomes = Stream.of(a.biomes, b.biomes)
                .map(Collection::stream)
                .flatMap(Function.identity());

        return new CreateOptions(
                biomes,
                a.canRespawn || b.canRespawn,
                a.skyLight || b.skyLight,
                a.waterVaporize || b.waterVaporize,
                b.fogColor, b.skyColor,
                a.daytime.equals("unset") ? a.daytime : b.daytime,
                a.enable || b.enable
        );
    }

    public boolean isValid() {
        return !biomes.isEmpty();
    }

    public Optional<Vec3d> getFogColor() {
        return Optional.ofNullable(this.fogColor);
    }

    public Optional<Vec3d> getSkyColor() {
        return Optional.ofNullable(this.skyColor);
    }

    public Set<Biome> getBiomes() {
        return this.biomes;
    }

}
