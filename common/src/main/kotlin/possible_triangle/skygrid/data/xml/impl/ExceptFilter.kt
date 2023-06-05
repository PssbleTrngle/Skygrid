package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.Filter
import possible_triangle.skygrid.data.xml.FilterOperator

@Serializable
@SerialName("except")
data class ExceptFilter(val filters: List<Filter>) : FilterOperator() {

    override fun test(block: Block, blocks: Registry<Block>): Boolean {
        return filters.none { it.test(block, blocks) }
    }

}