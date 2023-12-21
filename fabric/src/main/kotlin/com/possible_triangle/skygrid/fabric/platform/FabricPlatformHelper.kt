package com.possible_triangle.skygrid.fabric.platform

import com.google.common.collect.Maps
import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.platform.services.IPlatformHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty

class FabricPlatformHelper : IPlatformHelper {

    companion object {
        private val BLOCKS = Maps.newHashMap<String, Block>()

        fun register() {
            BLOCKS.forEach { (key: String?, value: Block) ->
                Registry.register(BuiltInRegistries.BLOCK,
                    ResourceLocation(SkygridConstants.MOD_ID, key),
                    value)
            }
        }
    }

    override val isDevelopmentEnvironment
        get() = FabricLoader.getInstance().isDevelopmentEnvironment

    override fun getTags(block: Block): Stream<ResourceLocation> {
        // TODO
        return Stream.empty()
    }

    override fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block> {
        return block().let {
            BLOCKS[id] = it
            ReadOnlyProperty { _, _ -> it }
        }
    }
}