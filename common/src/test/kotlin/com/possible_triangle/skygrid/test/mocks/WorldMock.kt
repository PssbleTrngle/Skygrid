package com.possible_triangle.skygrid.test.mocks

import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.GridConfig
import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.test.createBuiltinLookup
import com.possible_triangle.skygrid.world.BlockAccess
import com.possible_triangle.skygrid.xml.ReferenceContext
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import kotlin.test.assertEquals

object WorldMock {

    private val random = RandomSource.create()

    private val blocks = hashMapOf<BlockPos, BlockState>()
    private val nbt = hashMapOf<BlockPos, CompoundTag>()

    fun getBlock(pos: BlockPos) = blocks[pos]
    fun getNBT(pos: BlockPos) = nbt[pos]

    fun generate(vararg blocks: BlockProvider) = generate(GridConfig(blocks = ListWrapper(*blocks)))
    fun generate(config: GridConfig, references: IReferenceContext = ReferenceContext()) {
        config.validate(createBuiltinLookup(), references)
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