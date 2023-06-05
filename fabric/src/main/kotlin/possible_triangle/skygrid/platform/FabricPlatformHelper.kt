package possible_triangle.skygrid.platform

import com.google.common.collect.Maps
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.platform.services.IPlatformHelper
import kotlin.properties.ReadOnlyProperty

class FabricPlatformHelper : IPlatformHelper {

    companion object {
        private val BLOCKS = Maps.newHashMap<String, Block>()

        fun register() {
            BLOCKS.forEach { (key: String?, value: Block) ->
                Registry.register(Registry.BLOCK,
                    ResourceLocation(SkygridMod.MOD_ID, key),
                    value)
            }
        }
    }

    override val isDevelopmentEnvironment
        get() = FabricLoader.getInstance().isDevelopmentEnvironment

    override fun getTags(block: Block): Collection<ResourceLocation> {
        // TODO
        return emptyList()
    }

    override fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block> {
        return block().let {
            BLOCKS[id] = it
            ReadOnlyProperty { _, _ -> it }
        }
    }
}