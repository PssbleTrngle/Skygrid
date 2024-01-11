package com.possible_triangle.skygrid.api.xml.elements.extras

import com.possible_triangle.skygrid.api.extensions.random
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.*
import net.minecraft.core.Registry
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState

@Deprecated("use surround instead")
@Serializable
@SerialName("cardinal")
data class Cardinal(
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    val rotate: Boolean = true,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
) : Extra() {

    companion object {
        val DIRECTIONS = listOf(NORTH, EAST, SOUTH, WEST)
    }

    private fun getDirection(random: RandomSource): Direction {
        return DIRECTIONS.random(random)
    }

    override fun internalValidate(blocks: Registry<Block>): Boolean {
        return offset > 0
    }

    override fun transform(state: BlockState, random: RandomSource): BlockState {
        if (!rotate) return state
        val direction = getDirection(random)
        val rotations = DIRECTIONS.indexOf(direction)
        return state.rotate(Rotation.values()[rotations % 4])
    }

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos {
        val direction = getDirection(random)
        return pos.relative(direction, offset)
    }

}