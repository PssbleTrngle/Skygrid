package possibletriangle.skygrid.provider;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeManager;

import java.util.Random;
import java.util.stream.Stream;

public abstract class CollectionProvider extends BlockProvider {

    public abstract BlockProvider getProvider(Random random);

    @Override
    protected Block get(Random random) {
        return getProvider(random).get(random);
    }

    @Override
    protected BlockState apply(Random random, BlockState in) {
        return getProvider(random).apply(random, this.applyOwn(random, in));
    }

    @Override
    public final Stream<OffsetBlock> getExtras(Random random) {
        return getProvider(random).getOffsets(random);
    }
}
