package com.possible_triangle.skygrid.api.xml.elements.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import com.possible_triangle.skygrid.api.xml.elements.Filter
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator

@Serializable
@SerialName("except")
data class ExceptFilter(val filters: List<Filter>) : FilterOperator() {

    override fun test(block: Block, blocks: Registry<Block>): Boolean {
        return filters.none { it.test(block, blocks) }
    }

}