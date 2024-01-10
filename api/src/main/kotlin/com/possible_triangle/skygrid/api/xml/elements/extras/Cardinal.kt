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
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace.*
import net.minecraft.world.level.block.state.properties.BlockStateProperties

@Serializable
@SerialName("surround")
class Cardinal(
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    val transform: Boolean = true,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
    val directions: List<Direction> = HORIZONTAL_CARDINALS
) : Extra() {

    companion object {
        val HORIZONTAL_CARDINALS = listOf(SOUTH, WEST, NORTH, EAST)
    }

    private fun getDirection(random: RandomSource): Direction = directions.random(random)

    override fun internalValidate(blocks: Registry<Block>): Boolean = offset > 0

    override fun transform(state: BlockState, random: RandomSource): BlockState {
        if (!transform) return state

        val direction = getDirection(random)
        var finalState = state

        if (HORIZONTAL_CARDINALS.contains(direction))
            finalState = finalState.rotate(
                Rotation.values()[HORIZONTAL_CARDINALS.indexOf(direction) % 4]
            )
        else if (state.hasProperty(BlockStateProperties.FACING))
            finalState = finalState.setValue(
                DirectionalBlock.FACING,
                direction
            )

        if (state.hasProperty(FaceAttachedHorizontalDirectionalBlock.FACE))
            finalState = finalState.setValue(
                FaceAttachedHorizontalDirectionalBlock.FACE,
                when (direction) {
                    UP -> CEILING; DOWN -> FLOOR; else -> WALL
                }
            )

        return finalState
    }

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos =
        pos.relative(getDirection(random), offset)
}