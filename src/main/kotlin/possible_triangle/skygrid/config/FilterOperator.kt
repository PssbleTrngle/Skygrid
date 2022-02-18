package possible_triangle.skygrid.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.Block

@ExperimentalSerializationApi
@Serializable
abstract class FilterOperator {

    abstract fun test(block: Block): Boolean

}