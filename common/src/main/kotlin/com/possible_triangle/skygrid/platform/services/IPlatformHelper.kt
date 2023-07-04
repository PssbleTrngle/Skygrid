package com.possible_triangle.skygrid.platform.services

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import kotlin.properties.ReadOnlyProperty

interface IPlatformHelper {

    val isDevelopmentEnvironment: Boolean

    fun getTags(block: Block): Collection<ResourceLocation>

    fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block>

}