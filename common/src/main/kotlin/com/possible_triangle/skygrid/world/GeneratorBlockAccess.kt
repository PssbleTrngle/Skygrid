package com.possible_triangle.skygrid.world

import com.possible_triangle.skygrid.api.xml.elements.GridConfig
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import kotlin.math.max
import kotlin.math.min

class GeneratorBlockAccess(
    private val config: GridConfig,
    private val chunk: ChunkAccess,
) : BlockAccess(useBarrier = !config.gap.isPresent) {

    private val at = BlockPos.MutableBlockPos()
    val minY = max(chunk.minBuildHeight, config.minY)
    val maxY = min(chunk.maxBuildHeight, config.maxY)

    val origin = BlockPos(chunk.pos.minBlockX, -minY, chunk.pos.minBlockZ)

    private val gap = config.gap.map { it.block.defaultBlockState() }

    override fun setBlock(state: BlockState, pos: BlockPos) {
        val at = pos.offset(at)
        chunk.setBlockState(at, state, false)
    }

    override fun canReplace(pos: BlockPos): Boolean {
        val state = chunk.getBlockState(pos.offset(at))
        val isGap = config.gap.map { state.`is`(it.block) }.orElse(false)
        return isGap || state.isAir
    }

    override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
        with(pos.offset(at).offset(origin.x, 0, origin.z)) {
            nbt.putInt("x", x)
            nbt.putInt("y", y)
            nbt.putInt("z", z)
        }
        chunk.setBlockEntityNbt(nbt)
    }

    fun move(x: Int, y: Int, z: Int) {
        at.set(x, y, z)
    }

    fun shouldPlaceBlock() = config.distance.isBlock(origin.offset(at))

    fun fillGap() = gap.ifPresent { set(it) }

}
