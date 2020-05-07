package possibletriangle.skygrid.compat.jei.ingredient;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.dimension.DimensionType;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public enum DimensionIngredientRenderer implements IIngredientRenderer<DimensionType> {

    RENDERER;

    @Override
    public void render(int x, int y, @Nullable DimensionType ingredient) {
        if (ingredient != null) {
            ItemStack stack = new ItemStack(DimensionLoader.findIcon(ingredient.getRegistryName()));
            RenderSystem.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer font = getFontRenderer(minecraft, ingredient);
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            itemRenderer.renderItemAndEffectIntoGUI(null, stack, x, y);
            itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y, null);
            RenderSystem.disableBlend();
            RenderHelper.disableStandardItemLighting();
        }
    }

    @Override
    public List<String> getTooltip(DimensionType ingredient, ITooltipFlag flag) {
        return Collections.singletonList(DimensionIngredientHelper.HELPER.getDisplayName(ingredient));
    }
}
