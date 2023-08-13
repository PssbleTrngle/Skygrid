package com.possible_triangle.skygrid.api.world

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState

fun interface IBlockAccess {

    fun set(state: BlockState): Boolean = set(state, BlockPos(0, 0, 0))

    fun set(state: BlockState, pos: BlockPos): Boolean

    fun setNBT(pos: BlockPos, nbt: CompoundTag) {}

}

fun IBlockAccess.fork(transform: (BlockState) -> BlockState) = object : IBlockAccess {
    override fun set(state: BlockState, pos: BlockPos) = this@fork.set(transform(state), pos)

    override fun setNBT(pos: BlockPos, nbt: CompoundTag) = this@fork.setNBT(pos, nbt)
}