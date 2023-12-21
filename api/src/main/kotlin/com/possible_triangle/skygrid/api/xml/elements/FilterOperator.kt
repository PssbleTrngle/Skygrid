package com.possible_triangle.skygrid.api.xml.elements

import kotlinx.serialization.Serializable
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block

@Serializable
abstract class FilterOperator {

    abstract fun test(block: Block, blocks: HolderLookup.RegistryLookup<Block>): Boolean

}