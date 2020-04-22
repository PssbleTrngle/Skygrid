package possibletriangle.skygrid.provider;

import possibletriangle.skygrid.RandomCollection;

import java.util.Random;

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
}
