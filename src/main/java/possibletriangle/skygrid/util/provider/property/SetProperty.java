package possibletriangle.skygrid.util.provider.property;

import net.minecraft.state.IProperty;

import java.util.Optional;
import java.util.Random;

public class SetProperty extends PropertyProvider {

    private final String value;
    public SetProperty(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    protected <T extends Comparable<T>> Optional<T> getValue(IProperty<T> property, Random random) {
        return Optional.of(Optional.of(property.parseValue(value))
                .orElseGet(() -> property.parseValue(value.toLowerCase())))
                .orElseGet(() -> property.parseValue(value.toUpperCase()));
    }
}
