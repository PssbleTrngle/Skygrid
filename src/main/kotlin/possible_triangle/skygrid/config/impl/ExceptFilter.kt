package possible_triangle.skygrid.config.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.config.Filter
import possible_triangle.skygrid.config.FilterOperator

@Serializable
@SerialName("name")
data class NameFilter(val pattern: String): Filter() {

    @Transient
    private val regex = Regex.fromLiteral(pattern)

    override fun test(block: Block): Boolean {
        return regex.matches(block.registryName.toString())
    }

}

@Serializable
@SerialName("except")
data class ExceptFilter(val filters: List<Filter>) : FilterOperator() {

    override fun test(block: Block): Boolean {
        return filters.none { it.test(block) }
    }

}