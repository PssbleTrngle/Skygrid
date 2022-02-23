package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import kotlin.random.Random

@Serializable
abstract class Transformer {

    abstract fun apply(state: BlockState, random: Random): BlockState

}