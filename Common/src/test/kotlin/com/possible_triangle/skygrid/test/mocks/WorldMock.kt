package com.possible_triangle.skygrid.test.mocks

import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.world.BlockAccess
import kotlin.random.Random
import kotlin.test.assertEquals

object WorldMock {

    private val random = Random

    private val blocks = hashMapOf<BlockPos, BlockState>()
    private val nbt = hashMapOf<BlockPos, CompoundTag>()
    private val registryAccess = RegistryAccess.BUILTIN.get()

    fun getBlock(pos: BlockPos) = blocks[pos]
    fun getNBT(pos: BlockPos) = nbt[pos]

    fun generate(vararg blocks: BlockProvider) = generate(DimensionConfig(blocks = ListWrapper(*blocks)))
    fun generate(config: DimensionConfig) {
        config.validate(registryAccess)
        config.generate(random, access)
    }

    fun assert(pos: BlockPos, block: Block) {
        assertEquals(getBlock(pos)?.block, block)
    }

    fun reset() {
        blocks.clear()
        nbt.clear()
    }

    private val access = object : BlockAccess() {

        override fun setBlock(state: BlockState, pos: BlockPos) {
            blocks[pos] = state
        }

        override fun canReplace(pos: BlockPos): Boolean {
            return blocks[pos]?.isAir != false
        }

        override fun setNBT(pos: BlockPos, tag: CompoundTag) {
            nbt[pos] = tag
        }

    }

}