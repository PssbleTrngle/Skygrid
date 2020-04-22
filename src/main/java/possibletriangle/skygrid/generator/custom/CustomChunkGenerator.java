package possibletriangle.skygrid.generator.custom;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import possibletriangle.skygrid.data.loading.DimensionLoader;

public class CustomChunkGenerator extends ChunkGenerator<GenerationSettings> {

    private CreateOptions options;

    CreateOptions getOptions() {
        return this.options;
    }

    public CustomChunkGenerator(World world) {
        super(world, new CustomBiomeProvider(world.getDimension().getType(), world.getSeed()), new GenerationSettings());
        }

    @Override
    public void func_225551_a_(WorldGenRegion region, IChunk chunk) {}

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public void makeBase(IWorld world, IChunk chunk) {}

    @Override
    public int func_222529_a(int x, int z, Heightmap.Type type) {
        return 0;
    }
}
