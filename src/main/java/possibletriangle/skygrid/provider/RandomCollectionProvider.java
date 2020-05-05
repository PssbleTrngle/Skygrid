package possibletriangle.skygrid.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import possibletriangle.skygrid.RandomCollection;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class RandomCollectionProvider extends CollectionProvider implements INamed {

    @Nullable
    private final String name;
    private final RandomCollection<BlockProvider> children;
    public RandomCollectionProvider(RandomCollection<BlockProvider> children, @Nullable String name) {
        this.children = children;
        this.name = name;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
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
    public Stream<Pair<Float, Block>> allBlocks() {
        return this.children.stream().map(e -> e.getSecond().getPossibleBlocks().map(
                p -> new Pair<>(p.getFirst() * e.getFirst(), p.getSecond())
        )).flatMap(Function.identity());
    }

    @Override
    public Stream<Pair<Float, BlockProvider>> getAllProviders() {
        return children.stream();
    }
}
