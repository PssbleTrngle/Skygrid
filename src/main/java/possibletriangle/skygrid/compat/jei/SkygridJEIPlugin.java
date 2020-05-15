package possibletriangle.skygrid.compat.jei;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.compat.jei.ingredient.DimensionIngredient;
import possibletriangle.skygrid.compat.jei.ingredient.DimensionIngredientHelper;
import possibletriangle.skygrid.compat.jei.ingredient.DimensionIngredientRenderer;
import possibletriangle.skygrid.util.loading.DimensionConfig;
import possibletriangle.skygrid.util.loading.DimensionLoader;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class SkygridJEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(Skygrid.MODID, Skygrid.MODID);
    private static final MutableRegistry<DimensionType> DIMENSIONS = DimensionManager.getRegistry();

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    public Stream<Map.Entry<ResourceLocation,DimensionConfig>> configs() {
         return DimensionLoader.configs().filter(e -> DIMENSIONS.containsKey(e.getKey()));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        Collection<DimensionType> ingredients = configs()
                .map(e -> DIMENSIONS.getValue(e.getKey()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        registration.register(DimensionIngredient.TYPE, ingredients, DimensionIngredientHelper.HELPER, DimensionIngredientRenderer.RENDERER);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new ProbabilityCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        List<ProbabilityEntry> entries = configs()
                .map(e -> DIMENSIONS.getValue(e.getKey()).map(d -> new Pair<>(d, e.getValue())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(p -> p.getSecond().getUniqueBlocks()
                        .filter(e -> !new ItemStack(e.getKey()).isEmpty())
                        .map(e -> new ProbabilityEntry(e.getKey(), e.getValue(), p.getFirst()))
                )
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        registration.addRecipes(entries, ProbabilityCategory.ID);
    }
}
