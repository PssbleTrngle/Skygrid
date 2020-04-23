package possibletriangle.skygrid.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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

import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    public static Predicate<BlockPos> generateHere(ChunkPos chunk, BlockPos distance, BlockPos cluster) {

        int cx = cluster.getX();
        int cy = cluster.getY();
        int cz = cluster.getZ();

        int dx = distance.getX() + (cx - 1);
        int dy = distance.getY() + (cy - 1);
        int dz = distance.getZ() + (cz - 1);

        int chunkX = chunk.x * 16;
        int chunkZ = chunk.z * 16;

        return pos -> {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            return (Math.abs(y % dy) < cy) && (Math.abs((x + chunkX) % dx) < cx) && (Math.abs((z + chunkZ) % dz) < cz);
        };
    }

    @Override
    public void makeBase(IWorld world, IChunk chunk) {

        int maxY = Math.min(100, world.getHeight());

        BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
        BlockProvider fill = config.getFill();

        Predicate<BlockPos> generateHere = generateHere(chunk.getPos(), config.distance, config.cluster);
        int firstLevel = config.distance.getY() + (config.cluster.getY() - 1);

        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    BiConsumer<BlockPos, BlockState> generator = getGenerator(chunk, pos, random.nextLong());

                    if (generateHere.test(pos)) {
                        if (y < firstLevel) {
                            chunk.setBlockState(pos, BEDROCK, false);
                        } else {
                            BlockProvider block = this.config.randomProvider(random);
                            block.generate(generator, random);
                        }

                    } else if (chunk.getBlockState(pos).getBlock() == Blocks.AIR) {
                        fill.generate(generator, random);
                    }

                }

    }

    public BiConsumer<BlockPos, BlockState> getGenerator(IChunk chunk, BlockPos anchor, long seed) {
        BlockPos chunkPos = chunk.getPos().asBlockPos();
        Random r = new Random(seed);

        BiConsumer<BlockPos, BlockState> gen = getGenerator(
                (p, s) -> chunk.setBlockState(p, s, false),
                (p, t) -> chunk.addTileEntity(chunkPos.add(p), t),
                () -> config.randomLoot(r), world
        );

        return (p1, s1) -> gen.accept(p1.add(anchor), s1);
    }

    public static BiConsumer<BlockPos, BlockState> getGenerator(BiConsumer<BlockPos, BlockState> setBlock, BiConsumer<BlockPos, TileEntity> setTile, Supplier<ResourceLocation> loot, IWorld world) {
        long seed = world.getSeed();
        Rotation r = Rotation.randomRotation(new Random(seed));

        return (p, s) -> {

            BlockPos pos = p.rotate(r);
            setBlock.accept(pos, s.rotate(r));

            if (s.hasTileEntity())
                Optional.ofNullable(s.createTileEntity(world)).ifPresent(tile -> {

                    if (tile instanceof LockableLootTileEntity) {
                        ResourceLocation table = loot.get();
                        ((LockableLootTileEntity) tile).setLootTable(table, seed);
                    }

                    setTile.accept(pos, tile);
                });

        };
    }

    @Override
    public int func_222529_a(int x, int z, Heightmap.Type type) {
        return 0;
    }
}
