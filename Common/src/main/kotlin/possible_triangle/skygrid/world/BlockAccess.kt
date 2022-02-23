package possible_triangle.skygrid.world

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.Fallable
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.platform.Services

abstract class BlockAccess : IBlockAccess {

    protected abstract fun setBlock(state: BlockState, pos: BlockPos)
    protected abstract fun getBlock(pos: BlockPos): BlockState
    abstract fun setNBT(pos: BlockPos, nbt: CompoundTag)

    companion object {
        private val BARRIER = Services.PLATFORM.barrier.defaultBlockState()
    }

    private fun attemptSet(state: BlockState, pos: BlockPos) {
        if (getBlock(pos).isAir) setBlock(state, pos)
    }

    final override fun set(state: BlockState, pos: BlockPos) {
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