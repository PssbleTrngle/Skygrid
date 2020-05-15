package possibletriangle.skygrid.util.provider;

import net.minecraft.util.math.BlockPos;

public class OffsetBlock {

    public final BlockProvider provider;
    public final BlockPos offset;
    public final float probability;
    public final boolean shared;

    public OffsetBlock(BlockProvider provider, BlockPos offset, float probability, boolean shared) {
        this.probability = probability;
        this.offset = offset;
        this.provider = provider;
        this.shared = shared;
    }

}
