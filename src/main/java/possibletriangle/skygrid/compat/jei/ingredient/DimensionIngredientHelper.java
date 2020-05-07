package possibletriangle.skygrid.compat.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum DimensionIngredientHelper implements IIngredientHelper<DimensionType> {

    HELPER;

    @Nullable
    @Override
    public DimensionType getMatch(Iterable<DimensionType> iterable, DimensionType ingredient) {
        for (DimensionType i : iterable)
            if (i.getRegistryName().equals(ingredient.getRegistryName()))
                return i;
        return null;
    }

    @Override
    public String getDisplayName(DimensionType ingredient) {
        return Arrays.stream(ingredient.getRegistryName().getPath().split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    public String getUniqueId(DimensionType ingredient) {
        return ingredient.getRegistryName().toString();
    }

    @Override
    public String getWildcardId(DimensionType ingredient) {
        return getUniqueId(ingredient);
    }

    @Override
    public String getModId(DimensionType ingredient) {
        return ingredient.getRegistryName().getNamespace();
    }

    @Override
    public String getResourceId(DimensionType ingredient) {
        return ingredient.getRegistryName().getPath();
    }

    @Override
    public DimensionType copyIngredient(DimensionType ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable DimensionType ingredient) {
        if(ingredient == null) return "DimensionType is null";
        if(ingredient.getRegistryName() == null) return "DimensionType has no registry name";
        return null;
    }
}
