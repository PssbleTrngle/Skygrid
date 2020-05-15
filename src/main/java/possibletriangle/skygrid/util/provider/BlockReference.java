package possibletriangle.skygrid.util.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import possibletriangle.skygrid.util.loading.DimensionConfig;
import possibletriangle.skygrid.util.loading.DimensionLoader;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class BlockReference extends CollectionProvider {

    private final ResourceLocation name;
    @Nullable
    private BlockProvider ref;

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

    private BlockProvider getRef() {
        if(this.ref == null) this.ref = findRef();
        return this.ref;
    }

    @Override
    public BlockProvider getProvider(Random random) {
        return getRef();
    }

    @Override
    public Stream<Pair<Float, BlockProvider>> getAllProviders() {
        return Stream.of(new Pair<>(1F, getRef()));
    }

    @Override
    public Stream<Pair<Float, Block>> allBlocks() {
        return this.getRef().allBlocks();
    }
}
