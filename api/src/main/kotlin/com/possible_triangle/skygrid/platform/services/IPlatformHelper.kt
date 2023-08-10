package com.possible_triangle.skygrid.platform.services

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty

interface IPlatformHelper {

    val isDevelopmentEnvironment: Boolean

    fun getTags(block: Block): Stream<ResourceLocation>

    fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block>

}