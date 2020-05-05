package possibletriangle.skygrid.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.fixes.SpawnerEntityTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.StrongholdStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.Structures;
import possibletriangle.skygrid.RandomCollection;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.block.StiffAir;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;
import possibletriangle.skygrid.provider.BlockProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkygridChunkGenerator extends ChunkGenerator<SkygridSettings> {

    private DimensionConfig config;
    private final Random random;
    private final BlockPos END_PORTAL = new BlockPos(1, 0, 1);

    private void setConfig(DimensionConfig config) {
        this.config = config;
    }

    public SkygridChunkGenerator(World world) {
        super(world, WorldType.DEFAULT.createChunkGenerator(world).getBiomeProvider(), new SkygridSettings());
        DimensionLoader.subscribeConfig(world.getDimension().getType(), this::setConfig);
        this.random = new Random(world.getSeed());
    }

    @Override
    public void func_225551_a_(WorldGenRegion region, IChunk chunk) {
    }

    @Override
    public int func_222532_b(int p_222532_1_, int p_222532_2_, Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public int func_222531_c(int p_222531_1_, int p_222531_2_, Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public void func_225550_a_(BiomeManager p_225550_1_, IChunk p_225550_2_, GenerationStage.Carving p_225550_3_) {
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

    @Nullable
    @Override
    public BlockPos findNearestStructure(World world, String name, BlockPos pos, int radius, boolean idk) {
        Structure<?> structure = Feature.STRUCTURES.get(name.toLowerCase(Locale.ROOT));
        if(structure == Structures.STRONGHOLD) return END_PORTAL;
        return null;
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

        int chunkX = chunk.x * 16 - 1;
        int chunkZ = chunk.z * 16 - 1;

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
        int endX = END_PORTAL.getX();
        int endZ = END_PORTAL.getZ();
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        boolean isOverworld = world.getDimension().getType() == DimensionType.OVERWORLD;

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
                            if (isOverworld && endX == x + chunkX && endZ == z + chunkZ) {
                                DimensionLoader.findRef(new ResourceLocation("ender_portal"))
                                    .orElseThrow(() -> new NullPointerException("Could not find ender portal schema"))
                                    .generate(generator, random);
                            } else {
                                chunk.setBlockState(pos, BEDROCK, false);
                            }
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

        Supplier<ResourceLocation> spawns = () -> RandomCollection.from(
                getPossibleCreatures(EntityClassification.MONSTER, anchor).stream()
                        .map(e -> new Pair<>((float) e.itemWeight, e.entityType.getRegistryName()))
        ).next(r).orElseGet(EntityType.ZOMBIE::getRegistryName);

        return getGenerator(
                (p, s) -> chunk.setBlockState(p, s, false),
                p -> chunk.getBlockState(p).getBlock() == Blocks.AIR,
                (p, t) -> {
                    t.setPos(p.add(chunkPos));
                    chunk.addTileEntity(p.add(chunkPos), t);
                },
                () -> config.randomLoot(r),
                spawns,
                world, r, anchor
        );
    }

    public static BiConsumer<BlockPos, BlockState> getGenerator(BiConsumer<BlockPos, BlockState> setBlock, Predicate<BlockPos> isAir, BiConsumer<BlockPos, TileEntity> setTile, Supplier<ResourceLocation> loot, Supplier<ResourceLocation> mobs, IWorld world, Random random, BlockPos at) {
        Rotation r = Rotation.randomRotation(random);
        BlockState barrier = Skygrid.STIFF_AIR.get().getDefaultState();

        return (p, s) -> {

            BlockPos pos = p.rotate(r).add(at);
            setBlock.accept(pos, s.rotate(r));

            if (s.getBlock() instanceof FallingBlock && isAir.test(pos.down())) {
                setBlock.accept(pos.down(), barrier);
            }

            if (s.hasTileEntity())
                Optional.ofNullable(s.createTileEntity(world)).ifPresent(tile -> {

                    if (tile instanceof LockableLootTileEntity) {
                        ResourceLocation table = loot.get();
                        ((LockableLootTileEntity) tile).setLootTable(table, random.nextLong());
                    } else if (tile instanceof MobSpawnerTileEntity) {
                        CompoundNBT nbt = tile.write(new CompoundNBT());

                        ResourceLocation mob = mobs.get();
                        CompoundNBT data = new CompoundNBT();
                        data.putString("id", mob.toString());
                        nbt.put("SpawnData", data);

                        ListNBT potentials = new ListNBT();
                        CompoundNBT entry = new CompoundNBT();
                        entry.put("Entity", data);
                        entry.putInt("Weight", 1);
                        potentials.add(entry);
                        nbt.put("SpawnPotentials", potentials);
                        tile.read(nbt);
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
