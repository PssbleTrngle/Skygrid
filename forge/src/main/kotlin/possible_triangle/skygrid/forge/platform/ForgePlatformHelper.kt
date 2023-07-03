package possible_triangle.skygrid.forge.platform

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.fml.loading.FMLLoader
import possible_triangle.skygrid.forge.SkygridForge.BLOCKS
import possible_triangle.skygrid.platform.services.IPlatformHelper
import thedarkcolour.kotlinforforge.forge.registerObject
import javax.annotation.Nonnull
import kotlin.properties.ReadOnlyProperty

class ForgePlatformHelper : IPlatformHelper {

    override val isDevelopmentEnvironment: Boolean
        get() = !FMLLoader.isProduction()

    override fun getTags(block: Block): Collection<ResourceLocation> {
        // TODO
        return emptyList()
    }

    @Nonnull
    override fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block> {
        return BLOCKS.registerObject(id, block)
    }
}