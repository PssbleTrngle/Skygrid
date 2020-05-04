package possibletriangle.skygrid.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import java.util.Random;
import java.util.stream.Stream;

public class BlockReference extends CollectionProvider {

    private final ResourceLocation name;

    public BlockReference(ResourceLocation name) {
        this.name = name;
    }

    private BlockProvider findRef() {
        return DimensionLoader.findRef(this.name).orElse(DimensionConfig.FALLBACK_PROVIDER);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public BlockProvider getProvider(Random random) {
        return findRef();
    }

    @Override
    public Stream<Pair<Float, BlockProvider>> getAllProviders() {
        return Stream.of(new Pair<>(1F, findRef()));
    }

    @Override
    public Stream<Pair<Float, Block>> allBlocks() {
        return this.findRef().allBlocks();
    }
}
