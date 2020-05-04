package possibletriangle.skygrid.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGenerator;

public class SkygridWorldType extends WorldType {

    public SkygridWorldType() {
        super("skygrid");
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator(World world) {
        return new SkygridChunkGenerator(world);
    }

}
