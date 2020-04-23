package possibletriangle.skygrid.provider;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import possibletriangle.skygrid.provider.property.PropertyProvider;

import java.util.Collection;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BlockProvider {

    private Collection<PropertyProvider> properties = Lists.newArrayList();
    private Collection<OffsetBlock> offsets = Lists.newArrayList();

    public final Stream<OffsetBlock> getOffsets(Random random) {
        return Stream.of(this.offsets.stream(), this.getExtras(random)).flatMap(Function.identity());
    }

    public BlockProvider addProperties(Stream<PropertyProvider> properties) {
        properties.forEach(this.properties::add);
        return this;
    }

    public BlockProvider addOffsets(Stream<OffsetBlock> offsets) {
        offsets.forEach(this.offsets::add);
        return this;
    }

    public abstract boolean isValid();

    protected abstract Block get(Random random);

    protected BlockState apply(Random random, BlockState in) {
        return applyOwn(random, in);
    }

    protected final BlockState applyOwn(Random random, BlockState in) {
        return properties.stream().reduce(in, (s, p) -> p.apply(s, random), (a, b) -> a);
    }

    public Stream<OffsetBlock> getExtras(Random random) {
        return Stream.of();
    }

    public final void generate(BiConsumer<BlockPos, BlockState> generator, Random random) {
        long seed = random.nextLong();

        Block block = get(new Random(seed));
        BlockState state = apply(new Random(seed), block.getDefaultState());
        generator.accept(new BlockPos(0, 0, 0), state);

        Random shared = new Random(random.nextLong());

        this.getOffsets(new Random(seed))
                .filter(o -> (o.shared ? shared : random).nextFloat() <= o.probability)
                .forEachOrdered(o -> o.provider.generate((p, s) -> generator.accept(p.add(o.offset), s), o.shared ? shared : random));

    }

    protected abstract Stream<Block> allBlocks();

    public final Stream<Block> allOffsets() {
        return this.offsets.stream().map(o -> o.provider).map(BlockProvider::getPossibleBlocks).flatMap(Function.identity());
    }

    public final Stream<Block> getPossibleBlocks() {
        return Stream.of(this.allBlocks(), this.allOffsets()).flatMap(Function.identity());
    }

}
