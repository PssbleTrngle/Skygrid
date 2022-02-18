package possible_triangle.skygrid.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
abstract class Transformer {

    abstract fun apply(state: BlockState, random: Random): BlockState

}