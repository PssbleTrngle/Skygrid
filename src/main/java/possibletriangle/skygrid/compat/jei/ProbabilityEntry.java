package possibletriangle.skygrid.compat.jei;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;

public class ProbabilityEntry {

    public final Block block;
    public final float weight;
    public final DimensionType dimension;

    public ProbabilityEntry(Block block, float weight, DimensionType dimension) {
        this.block = block;
        this.weight = weight;
        this.dimension = dimension;
    }
}
