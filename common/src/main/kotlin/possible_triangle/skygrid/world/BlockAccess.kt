package possible_triangle.skygrid.world

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.Fallable
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.SkygridMod

abstract class BlockAccess(private val useBarrier: Boolean = true) : IBlockAccess {

    protected abstract fun setBlock(state: BlockState, pos: BlockPos)
    protected abstract fun canReplace(pos: BlockPos): Boolean
    abstract fun setNBT(pos: BlockPos, nbt: CompoundTag)

    companion object {
        private val BARRIER = SkygridMod.STIFF_AIR.defaultBlockState()
    }

    private fun attemptSet(state: BlockState, pos: BlockPos): Boolean {
        return canReplace(pos).also {
            if (it) setBlock(state, pos)
        }
    }

    final override fun set(state: BlockState, pos: BlockPos): Boolean {
        return attemptSet(state, pos).also { generated ->
            if (generated && useBarrier) {
                if (!state.fluidState.isEmpty) {
                    Direction.values().forEach {
                        attemptSet(BARRIER, pos.relative(it))
                    }
                } else if (state.block is Fallable) {
                    attemptSet(BARRIER, pos.below())
                }
            }
        }
    }

}