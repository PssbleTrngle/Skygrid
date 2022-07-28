package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.*
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import kotlin.random.Random

@Serializable
@SerialName("cardinal")
data class Cardinal(
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    val rotate: Boolean = true,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
) : Extra() {

    companion object {
        val DIRECTIONS = listOf(SOUTH, WEST, NORTH, EAST, )
    }

    private fun getDirection(random: Random): Direction {
        return DIRECTIONS.random(random)
    }

    override fun internalValidate(blocks: Registry<Block>): Boolean {
        return offset > 0
    }

    override fun transform(state: BlockState, random: Random): BlockState {
        val direction = getDirection(random)
        val rotations = DIRECTIONS.indexOf(direction)
        return state.rotate(Rotation.values()[rotations % 4])
    }

    override fun offset(pos: BlockPos, random: Random): BlockPos {
        val direction = getDirection(random)
        return pos.relative(direction, offset)
    }

}