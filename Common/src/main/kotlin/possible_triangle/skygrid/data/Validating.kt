package possible_triangle.skygrid.data

import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.FilterOperator

interface Validating {

    fun validate(
        blocks: Registry<Block>,
        tags: TagContainer,
        references: ReferenceContext,
        additionalFilters: List<FilterOperator> = emptyList()
    ): Boolean

}