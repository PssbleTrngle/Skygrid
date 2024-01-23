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
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.FACE
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace.*
import net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING

@Serializable
@SerialName("cardinal")
data class Cardinal(
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    val doTransform: Boolean = true,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
    val directions: List<Direction> = CARDINAL_DIRECTIONS,
) : Extra() {

    companion object {
        val CARDINAL_DIRECTIONS = listOf(NORTH, EAST, SOUTH, WEST)
    }

    private fun getDirection(random: RandomSource) = directions.random(random)

    override fun internalValidate(blocks: Registry<Block>) = offset > 0

    override fun transform(state: BlockState, random: RandomSource): BlockState {
        if (!doTransform) return state

        val direction = getDirection(random)
        var finalState = state

        if (state.hasProperty(FACE))
            finalState = finalState.setValue(
                FACE, when (direction) {
                    UP -> FLOOR
                    DOWN -> CEILING
                    else -> WALL
                }
            )

        if (state.hasProperty(FACING))
            finalState = finalState.setValue(FACING, direction.opposite)
        else if (direction.axis.isHorizontal)
            finalState = finalState.rotate(
                Rotation.values()[CARDINAL_DIRECTIONS.indexOf(direction) % 4]
            )

        return finalState
    }

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos = pos.relative(getDirection(random), offset)
}