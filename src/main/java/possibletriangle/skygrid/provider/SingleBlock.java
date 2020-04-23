package possibletriangle.skygrid.provider;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Random;
import java.util.stream.Stream;

public class SingleBlock extends BlockProvider {

    private static final IForgeRegistry<Block> BLOCKS = GameRegistry.findRegistry(Block.class);

    private final Block block;

    public SingleBlock(ResourceLocation name) {
        this(BLOCKS.containsKey(name) ? BLOCKS.getValue(name) : null);
    }

    public SingleBlock(Block block) {
        this.block = block;
    }

    @Override
    public boolean isValid() {
        return this.block != null;
    }

    @Override
    protected Block get(Random random) {
        return this.block;
    }

    @Override
    public Stream<Block> allBlocks() {
        return Stream.of(this.block);
    }
}
