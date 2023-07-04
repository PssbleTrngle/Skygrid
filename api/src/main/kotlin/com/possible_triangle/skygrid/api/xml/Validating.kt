package com.possible_triangle.skygrid.api.xml

import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator

interface Validating {

    fun validate(
        blocks: Registry<Block>,
        references: IReferenceContext,
        additionalFilters: List<FilterOperator> = emptyList()
    ): Boolean

}