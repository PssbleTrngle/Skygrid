package possibletriangle.skygrid.provider;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import possibletriangle.skygrid.provider.property.PropertyProvider;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BlockProvider {

    private PropertyProvider[] properties = {};
    private OffsetBlock[] offsets = {};

    public final Stream<OffsetBlock> getOffsets(Random random) {
        return Stream.of(
                Arrays.stream(this.offsets),
                this.getExtras(random)
        ).flatMap(Function.identity());
    }

    public void setProperties(Stream<PropertyProvider> properties) {
        this.properties = properties.toArray(PropertyProvider[]::new);
    }

    public void setOffets(Stream<OffsetBlock> offsets) {
        this.offsets = offsets.toArray(OffsetBlock[]::new);
    }

    public abstract boolean isValid();

    protected abstract Block get(Random random);

    protected BlockState apply(Random random, BlockState in) {
        return applyOwn(random, in);
    }

    protected final BlockState applyOwn(Random random, BlockState in) {
        return Arrays.stream(properties).reduce(in, (s, p) -> p.apply(s, random), (a, b) -> a);
    }

    public Stream<OffsetBlock> getExtras(Random random) {
        return Stream.of();
    }

    public final void generate(IChunk chunk, BlockPos at, Random random) {
        long seed = random.nextLong();

        Block block = get(new Random(seed));
        BlockState state = apply(new Random(seed), block.getDefaultState());
        chunk.setBlockState(at, state, false);

        Random shared = new Random(random.nextLong());

        this.getOffsets(new Random(seed))
                .filter(o -> (o.shared ? shared : random).nextFloat() <= o.probability)
                .forEachOrdered(o -> o.provider.generate(chunk, at.add(o.offset), o.shared ? shared : random));

    }

}
