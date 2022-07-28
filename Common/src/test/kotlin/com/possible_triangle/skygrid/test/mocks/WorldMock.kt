package com.possible_triangle.skygrid.test.mocks

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.world.BlockAccess
import possible_triangle.skygrid.world.IBlockAccess
import java.awt.image.ByteLookupTable
import kotlin.test.assertEquals

class WorldMock : BlockAccess() {

    private val blocks = hashMapOf<BlockPos, BlockState>()
    private val nbt = hashMapOf<BlockPos, CompoundTag>()

    fun getBlock(pos: BlockPos) = blocks[pos]
    fun getNBT(pos: BlockPos) = nbt[pos]

    override fun setBlock(state: BlockState, pos: BlockPos) {
        blocks[pos] = state
    }

    override fun canReplace(pos: BlockPos): Boolean {
        return blocks[pos]?.isAir != false
    }

    override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
        this.nbt[pos] = nbt
    }

    fun assert(pos: BlockPos, block: Block) {
        assertEquals(getBlock(pos)?.block, block)
    }

    fun reset() {
        blocks.clear()
        nbt.clear()
    }

}