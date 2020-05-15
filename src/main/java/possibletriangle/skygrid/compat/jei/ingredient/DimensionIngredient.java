package possibletriangle.skygrid.compat.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.dimension.DimensionType;

public enum DimensionIngredient implements IIngredientType<DimensionType> {

    TYPE;

    @Override
    public Class<? extends DimensionType> getIngredientClass() {
        return DimensionType.class;
    }
}
