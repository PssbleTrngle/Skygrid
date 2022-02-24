package possible_triangle.skygrid.platform

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.Tag
import net.minecraft.world.level.block.Block
import net.minecraftforge.fml.loading.FMLLoader
import possible_triangle.skygrid.SkygridForge.BLOCKS
import possible_triangle.skygrid.platform.services.IPlatformHelper
import thedarkcolour.kotlinforforge.forge.registerObject
import javax.annotation.Nonnull
import kotlin.properties.ReadOnlyProperty

class ForgePlatformHelper : IPlatformHelper {

    override val isDevelopmentEnvironment: Boolean
        get() = !FMLLoader.isProduction()

    override fun getTags(block: Block): Collection<ResourceLocation> {
        return block.tags
    }

    @Nonnull
    override fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block> {
        return BLOCKS.registerObject(id, block)
    }

    @Nonnull
    override fun createBlockTag(id: ResourceLocation): Tag.Named<Block> {
        return BlockTags.createOptional(id)
    }
}