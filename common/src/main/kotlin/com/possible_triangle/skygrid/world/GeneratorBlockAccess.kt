package com.possible_triangle.skygrid.world

import com.possible_triangle.skygrid.api.xml.elements.GridConfig
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import kotlin.math.max
import kotlin.math.min

class GeneratorBlockAccess(
    private val config: GridConfig,
    val chunkPos: ChunkPos,
    private val region: WorldGenRegion,
) : BlockAccess(useBarrier = !config.gap.isPresent) {

    private val at = BlockPos.MutableBlockPos()
    val minY = max(region.minBuildHeight, config.minY)
    val maxY = min(region.maxBuildHeight, config.maxY)

    val origin = BlockPos(chunkPos.minBlockX, -minY, chunkPos.minBlockZ)

    private val gap = config.gap.map { it.block.defaultBlockState() }

    private fun chunkAt(pos: BlockPos): ChunkAccess {
        return region.getChunk(pos.offset(origin))
    }

    override fun setBlock(state: BlockState, pos: BlockPos) {
        val at = pos.offset(at)
        chunkAt(at).setBlockState(at, state, false)
    }

    override fun canReplace(pos: BlockPos): Boolean {
        val at = pos.offset(at)
        val state = chunkAt(at).getBlockState(at)
        val isGap = config.gap.map { state.`is`(it.block) }.orElse(false)
        return isGap || state.isAir
    }

    override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
        val at = pos.offset(at)
        with(at.offset(origin.x, 0, origin.z)) {
            nbt.putInt("x", x)
            nbt.putInt("y", y)
            nbt.putInt("z", z)
        }
        chunkAt(at).setBlockEntityNbt(nbt)
    }

    fun move(x: Int, y: Int, z: Int) {
        at.set(x, y, z)
    }

    fun shouldPlaceBlock() = config.distance.isBlock(origin.offset(at))

    fun fillGap() = gap.ifPresent { set(it) }

}
