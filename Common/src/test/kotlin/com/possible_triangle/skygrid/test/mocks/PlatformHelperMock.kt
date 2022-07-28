package com.possible_triangle.skygrid.test.mocks

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import possible_triangle.skygrid.platform.services.IPlatformHelper
import kotlin.properties.ReadOnlyProperty

class PlatformHelperMock : IPlatformHelper {
    override val isDevelopmentEnvironment = true

    override fun getTags(block: Block): Collection<ResourceLocation> = emptyList()

    override fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block> {
        return ReadOnlyProperty<Any?, Block> { _, _ -> Blocks.CAVE_AIR }
    }

    override fun createBlockTag(id: ResourceLocation): TagKey<Block> {
        return TagKey.create(Registry.BLOCK_REGISTRY, id)
    }
}