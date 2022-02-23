package possible_triangle.skygrid.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import possible_triangle.skygrid.SkygridForge;
import possible_triangle.skygrid.platform.services.IPlatformHelper;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.Collection;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Collection<ResourceLocation> getTags(Block block) {
        return block.getTags();
    }

    @Override
    public Block getBarrier() {
        return SkygridForge.INSTANCE.getStiffAir();
    }

    @Override
    public Tag.Named<Block> createBlockTag(ResourceLocation id) {
        return BlockTags.createOptional(id);
    }
}
