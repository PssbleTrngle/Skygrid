package possibletriangle.skygrid.data.loading;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import possibletriangle.skygrid.RandomCollection;
import possibletriangle.skygrid.generator.custom.CreateOptions;
import possibletriangle.skygrid.provider.BlockProvider;
import possibletriangle.skygrid.provider.RandomCollectionProvider;
import possibletriangle.skygrid.provider.SingleBlock;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class DimensionConfig {

    public static final int DEFAULT_DISTANCE = 4;
    public static final int DEFAULT_CLUSTER = 1;

    public static final BlockProvider FALLBACK_PROVIDER = new SingleBlock(Blocks.BEDROCK);
    public static final BlockProvider DEFAULT_FILL = new SingleBlock(Blocks.AIR);
    public static final DimensionConfig FALLBACK = new DimensionConfig(
            false, null,
            new RandomCollection<>(FALLBACK_PROVIDER),
            new BlockPos(DEFAULT_DISTANCE, DEFAULT_DISTANCE, DEFAULT_DISTANCE),
            new BlockPos(DEFAULT_CLUSTER, DEFAULT_CLUSTER, DEFAULT_CLUSTER),
            DEFAULT_FILL
    );

    private final boolean replace;

    @Nullable
    private final CreateOptions create;

    private final RandomCollection<BlockProvider> providers;

    @Nullable
    private final BlockProvider fill;

    private final BlockPos readDistance;
    private final BlockPos readCluster;
    public final BlockPos distance;
    public final BlockPos cluster;

    private BlockPos defaultPos(BlockPos pos, int def) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new BlockPos(x == 0 ? def : x, y == 0 ? def : y, z == 0 ? def : z);
    }

    public DimensionConfig(boolean replace, @Nullable  CreateOptions create, RandomCollection<BlockProvider> providers, BlockPos distance, BlockPos cluster, BlockProvider fill) {
        this.replace = replace;
        this.create = create;
        this.providers = providers;
        this.readDistance = distance;
        this.readCluster = cluster;
        this.fill = fill;

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

        RandomCollection<BlockProvider> merged = new RandomCollection<BlockProvider>()
                .addAll(a.providers)
                .addAll(b.providers);

        return new DimensionConfig(
                false,
                CreateOptions.merge(a.create, b.create),
                merged,
                mergePos(a.readDistance, b.readDistance),
                mergePos(a.readCluster, b.readCluster),
                Optional.of(new RandomCollectionProvider(
                        new RandomCollection<>(Stream.of(a.fill, b.fill)
                                .filter(Objects::nonNull)
                                .toArray(BlockProvider[]::new)))
                        ).filter(RandomCollectionProvider::isValid).orElse(null)
        );
    }

    public Optional<CreateOptions> getCreateOptions() {
        return Optional.ofNullable(this.create);
    }

    public boolean isValid() {
        return !providers.empty();
    }

    public BlockProvider getFill() {
        return Optional.ofNullable(this.fill).orElse(DEFAULT_FILL);
    }

    public BlockProvider randomProvider(Random random) {
        return this.providers.next(random).orElseThrow(() -> new NullPointerException("Collection should not be empty"));
    }

}
