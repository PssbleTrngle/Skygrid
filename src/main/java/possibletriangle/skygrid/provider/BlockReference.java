package possibletriangle.skygrid.provider;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Random;

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
}
