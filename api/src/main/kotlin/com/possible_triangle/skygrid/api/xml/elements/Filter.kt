package com.possible_triangle.skygrid.api.xml.elements

import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block

@Serializable
abstract class Filter {

    abstract fun test(block: Block, blocks: Registry<Block>): Boolean

}