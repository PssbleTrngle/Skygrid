package possibletriangle.skygrid.generator.custom;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.common.ModDimension;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import java.util.function.BiFunction;

public class CustomDimension extends OverworldDimension {

    public CustomDimension(World world, DimensionType type) {
        super(world, type);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        return new CustomChunkGenerator(world);
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return super.getFogColor(celestialAngle, partialTicks);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    public static ModDimension create(ResourceLocation name) {
        return new ModDimension() {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
                return (w, t) -> new CustomDimension(w, t);
            }
        }.setRegistryName(name);
    }
}
