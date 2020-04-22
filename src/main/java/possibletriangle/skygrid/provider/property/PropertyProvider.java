package possibletriangle.skygrid.provider.property;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public abstract class PropertyProvider {

    private final String key;

    public PropertyProvider(String key) {
        this.key = key;
    }

    private <T extends Comparable<T>> Optional<IProperty<T>> getProperty(BlockState state) {
        return state.getProperties().stream()
                .filter(p -> p.getName().equalsIgnoreCase(key))
                .map(p -> (IProperty<T>) p)
                .findAny();
    }

    public final BlockState apply(BlockState in, Random random) {
        return getProperty(in).map(p -> getValue(p, random).map(v  -> in.with(p, v))).flatMap(Function.identity()).orElse(in);
    }

    protected abstract <T extends Comparable<T>> Optional<T> getValue(IProperty<T> property, Random random);

}
