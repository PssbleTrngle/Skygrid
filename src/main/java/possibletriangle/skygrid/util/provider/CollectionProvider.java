package possibletriangle.skygrid.util.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Random;
import java.util.stream.Stream;

public abstract class CollectionProvider extends BlockProvider {

    public abstract BlockProvider getProvider(Random random);

    public abstract Stream<Pair<Float, BlockProvider>> getAllProviders();

    @Override
    protected Block get(Random random) {
        return getProvider(random).get(random);
    }

    @Override
    protected BlockState apply(Random random, BlockState in) {
        return getProvider(random).apply(random, this.applyOwn(random, in));
    }

    @Override
    public final Stream<OffsetBlock> getExtras(Random random) {
        return getProvider(random).getOffsets(random);
    }
}
