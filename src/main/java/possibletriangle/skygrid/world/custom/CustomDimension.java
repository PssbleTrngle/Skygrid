package possibletriangle.skygrid.world.custom;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ModDimension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomDimension extends OverworldDimension {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    private CreateOptions options;

    public Optional<CreateOptions> getOptions() {
        return Optional.ofNullable(options);
    }

    @Override
    public boolean hasSkyLight() {
        return getOptions().map(c -> c.skyLight).orElse(false);
    }

    @Override
    public boolean doesWaterVaporize() {
        return getOptions().map(c -> c.waterVaporize).orElse(false);
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return getOptions().map(c -> !c.waterVaporize).orElse(false);
    }

    @Override
    public boolean isDaytime() {
        String daytime = getOptions().map(c -> c.daytime).orElse("unset");
        switch (daytime) {
            case "day":
                return true;
            case "night":
                return false;
            default:
                return super.isDaytime();
        }
    }

    public static final String NO_CREATE_OPTIONS = "Only custom dimensions for registered skygrid configs with create options are allowed";

    private void reloadConfig(DimensionConfig config) {
        this.options = config.getCreateOptions().orElseThrow(() -> new IllegalStateException(NO_CREATE_OPTIONS));
    }

    public CustomDimension(World world, DimensionType type) {
        super(world, type);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        DimensionLoader.subscribeConfig(world.getDimension().getType(), this::reloadConfig);
        return new CustomChunkGenerator(world, () -> options);
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return getOptions().map(CreateOptions::getFogColor).flatMap(Function.identity()).orElseGet(() -> super.getFogColor(celestialAngle, partialTicks));
    }

    @OnlyIn(Dist.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return getOptions().map(CreateOptions::getFogColor).flatMap(Function.identity()).isPresent();
    }

    @Override
    public boolean isSkyColored() {
        return getOptions().map(CreateOptions::getSkyColor).flatMap(Function.identity()).isPresent();
    }

    @Override
    public boolean canRespawnHere() {
        return getOptions().map(o -> o.canRespawn).orElse(false);
    }

    public static ModDimension create(ResourceLocation name) {
        LOGGER.info("Created custom dimension '{}'", name.toString());

        return new ModDimension() {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
                return CustomDimension::new;
            }
        }.setRegistryName(name);
    }
}
