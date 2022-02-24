package possible_triangle.skygrid.mixin;

import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WorldPreset.class)
public interface WorldPresetMixin {

    @Accessor("PRESETS")
    static List<WorldPreset> presets() {
        throw new AssertionError();
    }

}
