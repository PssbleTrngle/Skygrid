package possibletriangle.skygrid.util.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.stream.Stream;

public class SingleBlock extends BlockProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final IForgeRegistry<Block> BLOCKS = GameRegistry.findRegistry(Block.class);

    private final Block block;

    public SingleBlock(ResourceLocation name) {
        this(BLOCKS.containsKey(name) ? BLOCKS.getValue(name) : null);
        if(this.block == null && ModList.get().isLoaded(name.getNamespace()))
            LOGGER.warn("Could not find the block '{}' even tho the mod is loaded", name.toString());
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
        return this.getBlock();
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public Stream<Pair<Float, Block>> allBlocks() {
        return Stream.of(new Pair<>(1F, this.block));
    }
}
