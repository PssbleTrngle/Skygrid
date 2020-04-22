package possibletriangle.skygrid.generator;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGenerator;
import possibletriangle.skygrid.data.loading.DimensionConfig;
import possibletriangle.skygrid.data.loading.DimensionLoader;

public class SkygridWorldType extends WorldType {

    public SkygridWorldType() {
        super("skygrid");
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator(World world) {
        return new SkygridChunkGenerator(world);
    }

}
