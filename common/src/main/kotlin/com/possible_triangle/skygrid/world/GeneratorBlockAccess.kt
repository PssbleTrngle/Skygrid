package com.possible_triangle.skygrid.world

import com.possible_triangle.skygrid.api.xml.elements.DimensionConfig
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess

class GeneratorBlockAccess(
    private val config: DimensionConfig,
    private val chunk: ChunkAccess,
    private val at: BlockPos,
    private val origin: BlockPos,
) : BlockAccess(useBarrier = !config.gap.isPresent) {

    override fun setBlock(state: BlockState, pos: BlockPos) {
        val at = pos.offset(at)
        chunk.setBlockState(at, state, false)
    }

    override fun canReplace(pos: BlockPos): Boolean {
        val state = chunk.getBlockState(pos.offset(at))
        return config.gap.map {
            state.`is`(it.block)
        }.orElseGet {
            state.isAir
        }
    }

    override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
        with(pos.offset(at).offset(origin.x, 0, origin.z)) {
            nbt.putInt("x", x)
            nbt.putInt("y", y)
            nbt.putInt("z", z)
        }
        chunk.setBlockEntityNbt(nbt)
    }

}
