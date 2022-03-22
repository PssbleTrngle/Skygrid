package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block

@Serializable
abstract class FilterOperator {

    abstract fun test(block: Block, blocks: Registry<Block>): Boolean

}