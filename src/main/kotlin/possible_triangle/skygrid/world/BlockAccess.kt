package possible_triangle.skygrid.world

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Fallable
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.SkygridMod

class BlockAccess(
    private val setBlock: (state: BlockState, pos: BlockPos) -> Unit,
    private val isAir: (pos: BlockPos) -> Boolean,
) {

    companion object {
        private val BARRIER = SkygridMod.STIFF_AIR.defaultBlockState()
    }

    fun set(state: BlockState) {
        set(state, BlockPos(0, 0, 0))
    }

    private fun attemptSet(state: BlockState, pos: BlockPos) {
        if (isAir(pos)) setBlock(state, pos)
//        else setBlock(state, pos)
    }

    fun set(state: BlockState, pos: BlockPos) {
        attemptSet(state, pos)
        if (!state.fluidState.isEmpty) {
            Direction.values().forEach {
                attemptSet(BARRIER, pos.relative(it))
            }
        } else if (state.block is Fallable) {
            attemptSet(BARRIER, pos.below())
        }
    }

}