package possibletriangle.skygrid.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;
import possibletriangle.skygrid.provider.BlockProvider;

import java.util.Random;

public class SkygridChunkGenerator extends ChunkGenerator<SkygridSettings> {

    private DimensionConfig config;
    private final Random random;

    private void setConfig(DimensionConfig config) {
        this.config = config;
    }

    public SkygridChunkGenerator(World world) {
        super(world, WorldType.DEFAULT.createChunkGenerator(world).getBiomeProvider(), new SkygridSettings());
        DimensionLoader.findConfig(world.getDimension().getType(), this::setConfig);
        this.random = new Random(world.getSeed());
    }

    @Override
    public void func_225551_a_(WorldGenRegion region, IChunk chunk) {
    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public void decorate(WorldGenRegion region) {
    }

    @Override
    public boolean hasStructure(Biome biome, Structure<? extends IFeatureConfig> structure) {
        return false;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public void makeBase(IWorld world, IChunk chunk) {

        int maxY = Math.min(100, world.getHeight());

        int cx = config.cluster.getX();
        int cy = config.cluster.getY();
        int cz = config.cluster.getZ();

        int dx = config.distance.getX() + (cx - 1);
        int dy = config.distance.getY() + (cy - 1);
        int dz = config.distance.getZ() + (cz - 1);

        int chunkX = chunk.getPos().x * 16;
        int chunkZ = chunk.getPos().z * 16;

        BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
        BlockProvider fill = config.getFill();

        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if ((Math.abs(y % dy) < cy) && (Math.abs((x + chunkX) % dx) < cx) && (Math.abs((z + chunkZ) % dz) < cz)) {
                        if (y < dy) {
                            chunk.setBlockState(pos, BEDROCK, false);
                        } else {
                            BlockProvider block = this.config.randomProvider(random);
                            block.generate(chunk, pos, random);
                        }

                    } else if(chunk.getBlockState(pos).getBlock() == Blocks.AIR) {
                        fill.generate(chunk, pos, random);
                    }

                }

    }

    @Override
    public int func_222529_a(int x, int z, Heightmap.Type type) {
        return 0;
    }
}
