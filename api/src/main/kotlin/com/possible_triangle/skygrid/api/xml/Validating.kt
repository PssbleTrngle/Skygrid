package com.possible_triangle.skygrid.api.xml

import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.platform.Services
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block

interface Validating {

    fun validate(
        blocks: HolderLookup.RegistryLookup<Block>,
        references: IReferenceContext,
        additionalFilters: List<FilterOperator> = emptyList(),
    ): Boolean

}

internal fun Boolean.warnInvalid(message: () -> String) = also { valid ->
    if (!valid && Services.CONFIGS.server.warnInvalid) {
        SkygridConstants.LOGGER.warn(message())
    }
}