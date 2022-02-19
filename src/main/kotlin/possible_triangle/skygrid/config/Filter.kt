package possible_triangle.skygrid.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.Block

@Serializable
abstract class Filter {

    abstract fun test(block: Block): Boolean

}