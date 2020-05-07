package possibletriangle.skygrid.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.dimension.DimensionType;
import possibletriangle.skygrid.Skygrid;
import possibletriangle.skygrid.compat.jei.ingredient.DimensionIngredient;

public class ProbabilityCategory implements IRecipeCategory<ProbabilityEntry> {

    public static final ResourceLocation ID = new ResourceLocation(Skygrid.MODID, "probability");

    private final IDrawable slot, icon, background;
    private final String localizedName;

    public ProbabilityCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(96, 22);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.GOLD_ORE));
        this.localizedName = I18n.format("gui.skygrid.category.probability");

        this.slot = guiHelper.getSlotDrawable();
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends ProbabilityEntry> getRecipeClass() {
        return ProbabilityEntry.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ProbabilityEntry entry, IIngredients ingredients) {
        ingredients.setInput(DimensionIngredient.TYPE, entry.dimension);
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(entry.block));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, ProbabilityEntry entry, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        stacks.init(0, false, 3, 3);
        stacks.setBackground(0, this.slot);

        IGuiIngredientGroup<DimensionType> dimensions = layout.getIngredientsGroup(DimensionIngredient.TYPE);
        dimensions.init(1, true, 78, 4);

        dimensions.set(ingredients);
        stacks.set(ingredients);
    }

    @Override
    public void draw(ProbabilityEntry entry, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();

        int left = 36;
        String probability = String.format("%.3f", entry.weight * 100) + "%";
        minecraft.fontRenderer.drawString(probability, left, 8F, -8355712);
    }
}
