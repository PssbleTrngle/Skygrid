package possibletriangle.skygrid.provider.property;

import net.minecraft.state.IProperty;

import java.util.*;
import java.util.stream.Collectors;

public class CycleProperty extends PropertyProvider {

    public CycleProperty(String key) {
        super(key);
    }

    @Override
    protected <T extends Comparable<T>> Optional<T> getValue(IProperty<T> property, Random random) {
        List<T> values = new ArrayList<>(property.getAllowedValues());
        if(values.isEmpty()) return Optional.empty();
        return Optional.of(values.get((int) Math.floor(Math.random() * values.size())));
    }
}
