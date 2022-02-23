package possible_triangle.skygrid.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

import java.util.Collection;

public interface IPlatformHelper {

    boolean isDevelopmentEnvironment();

    Collection<ResourceLocation> getTags(Block block);

    Block getBarrier();

    Tag.Named<Block> createBlockTag(ResourceLocation id);

}
