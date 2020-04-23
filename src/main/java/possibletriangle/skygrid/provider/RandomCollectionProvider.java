package possibletriangle.skygrid.provider;

import net.minecraft.block.Block;
import possibletriangle.skygrid.RandomCollection;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class RandomCollectionProvider extends CollectionProvider {

    private final RandomCollection<BlockProvider> children;
    public RandomCollectionProvider(RandomCollection<BlockProvider> children) {
        this.children = children;
    }

    @Override
    public BlockProvider getProvider(Random random) {
        return this.children.next(random).orElseThrow(() -> new NullPointerException("Collection should no be isEmpty"));
    }

    @Override
    public boolean isValid() {
        return !this.children.isEmpty();
    }

    @Override
    public Stream<Block> allBlocks() {
        return this.children.all().stream().map(BlockProvider::allBlocks).flatMap(Function.identity());
    }
}
