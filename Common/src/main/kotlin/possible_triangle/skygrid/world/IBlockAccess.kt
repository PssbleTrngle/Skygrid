package possible_triangle.skygrid.world

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

fun interface IBlockAccess {

    fun set(state: BlockState): Boolean = set(state, BlockPos.ZERO)

    fun set(state: BlockState, pos: BlockPos): Boolean

}