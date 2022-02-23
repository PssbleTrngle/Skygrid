package possible_triangle.skygrid.platform;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import possible_triangle.skygrid.platform.services.IPlatformHelper;

import java.util.Collection;
import java.util.Collections;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Collection<ResourceLocation> getTags(Block block) {
        // TODO
        return Collections.emptyList();
    }

    @Override
    public Block getBarrier() {
        // TODO
        return Blocks.GLASS;
    }

    @Override
    public Tag.Named<Block> createBlockTag(ResourceLocation id) {
        return TagFactory.BLOCK.create(id);
    }

}
