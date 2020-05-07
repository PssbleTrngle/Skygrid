package possibletriangle.skygrid.data.loading;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.skygrid.RandomCollection;
import possibletriangle.skygrid.world.custom.CreateOptions;
import possibletriangle.skygrid.provider.BlockProvider;
import possibletriangle.skygrid.provider.RandomCollectionProvider;
import possibletriangle.skygrid.provider.SingleBlock;
import possibletriangle.skygrid.provider.property.PropertyProvider;
import possibletriangle.skygrid.provider.property.SetProperty;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class DimensionConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final int DEFAULT_DISTANCE = 4;
    public static final int DEFAULT_CLUSTER = 1;

    public static final BlockPos DEFAULT_DISTANCE_POS = new BlockPos(DEFAULT_DISTANCE, DEFAULT_DISTANCE, DEFAULT_DISTANCE);
    public static final BlockPos DEFAULT_CLUSTER_POS = new BlockPos(DEFAULT_CLUSTER, DEFAULT_CLUSTER, DEFAULT_CLUSTER);

    public static final BlockProvider FALLBACK_PROVIDER = new SingleBlock(Blocks.BEDROCK);
    public static final BlockProvider DEFAULT_FILL = new SingleBlock(Blocks.AIR);
    public static final DimensionConfig FALLBACK = new DimensionConfig(
            false, null,
            new RandomCollection<>(FALLBACK_PROVIDER),
            DEFAULT_DISTANCE_POS,
            DEFAULT_CLUSTER_POS,
            DEFAULT_FILL,
            new RandomCollection<>(LootTables.EMPTY)
    );

    public Stream<PropertyProvider> defaultProperties() {
        return Stream.of(new SetProperty("persistent", "true"));
    }

    private final boolean replace;

    @Nullable
    private final CreateOptions create;

    public final RandomCollection<BlockProvider> providers;

    @Nullable
    private final BlockProvider fill;

    private final BlockPos readDistance;
    private final BlockPos readCluster;
    public final BlockPos distance;
    public final BlockPos cluster;

    private final RandomCollection<ResourceLocation> loot;

    private BlockPos defaultPos(BlockPos pos, int def) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new BlockPos(x == 0 ? def : x, y == 0 ? def : y, z == 0 ? def : z);
    }

    public DimensionConfig(boolean replace, @Nullable CreateOptions create, RandomCollection<BlockProvider> providers, BlockPos distance, BlockPos cluster, BlockProvider fill, RandomCollection<ResourceLocation> loot) {
        this.replace = replace;
        this.create = create;
        this.providers = providers;
        this.readDistance = distance;
        this.readCluster = cluster;
        this.fill = fill;
        this.loot = loot;

        this.distance = defaultPos(distance, DEFAULT_DISTANCE);
        this.cluster = defaultPos(cluster, DEFAULT_CLUSTER);
    }

    public static BlockPos mergePos(BlockPos a, BlockPos b) {
        int x = a.getX() == 0 ? b.getX() : a.getX();
        int y = a.getY() == 0 ? b.getY() : a.getY();
        int z = a.getZ() == 0 ? b.getZ() : a.getZ();
        return new BlockPos(x, y, z);
    }

    public static DimensionConfig merge(DimensionConfig a, DimensionConfig b) {
        if (b.replace) return b;
        if (a.replace) return a;

        RandomCollection<BlockProvider> providers = new RandomCollection<BlockProvider>()
                .addAll(a.providers)
                .addAll(b.providers);

        RandomCollection<ResourceLocation> loot = new RandomCollection<ResourceLocation>()
                .addAll(a.loot)
                .addAll(b.loot);

        return new DimensionConfig(
                false,
                CreateOptions.merge(a.create, b.create),
                providers,
                mergePos(a.readDistance, b.readDistance),
                mergePos(a.readCluster, b.readCluster),
                Optional.of(new RandomCollectionProvider(
                        new RandomCollection<>(Stream.of(a.fill, b.fill)
                                .filter(Objects::nonNull)
                                .toArray(BlockProvider[]::new)), null)
                ).filter(RandomCollectionProvider::isValid).orElse(null),
                loot
        );
    }

    public Optional<CreateOptions> getCreateOptions() {
        return Optional.ofNullable(this.create).filter(c -> c.enable);
    }

    public boolean isValid(ResourceLocation name) {
        if (providers.isEmpty()) {
            LOGGER.warn("Skygrid config for {} has no valid providers", name);
            return false;
        } else if (loot.isEmpty()) {
            LOGGER.warn("Skygrid config for {} has no valid loot tables", name);
            return false;
        }
        return true;
    }

    public BlockProvider getFill() {
        return Optional.ofNullable(this.fill).orElse(DEFAULT_FILL);
    }

    public BlockProvider randomProvider(Random random) {
        return this.providers.next(random).orElseThrow(() -> new NullPointerException("Provider collection should not be isEmpty"));
    }

    public ResourceLocation randomLoot(Random random) {
        return this.loot.next(random).orElseThrow(() -> new NullPointerException("Loot collection should not be empty"));
    }

    public Stream<Pair<Float, Block>> getPossibleBlocks() {
        return this.providers.stream().map(e -> e.getSecond().getPossibleBlocks().map(
                p -> new Pair<>(p.getFirst() * e.getFirst(), p.getSecond())
        )).flatMap(Function.identity());
    }

    public Stream<Map.Entry<Block,Float>> getUniqueBlocks() {
        return getPossibleBlocks().reduce(new HashMap<Block, Float>(), (map, pair) -> {
            Block b = pair.getSecond();
            map.put(b, map.getOrDefault(b, 0F) + pair.getFirst());
            return map;
        }, (a, b) -> b)
                .entrySet().stream()
                .sorted((a, b) -> {
                    if (a.getValue().equals(b.getValue())) return 0;
                    else return a.getValue() < b.getValue() ? 1 : -1;
                });
    }

}
