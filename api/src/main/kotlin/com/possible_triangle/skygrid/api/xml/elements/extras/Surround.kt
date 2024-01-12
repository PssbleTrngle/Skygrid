package com.possible_triangle.skygrid.api.xml.elements.extras

import com.possible_triangle.skygrid.api.extensions.random
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.byName
import net.minecraft.core.Registry
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.level.block.state.properties.BlockStateProperties

@Serializable
@SerialName("surround")
class Surround(
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
    val transform: Boolean = true,
    @SerialName("direction")
    val directionNames: List<String> = emptyList(),
) : Extra() {

    companion object {
        val ALL_DIRECTIONS = Direction.values().toList()
        val CARDINAL_DIRECTIONS = Cardinal.DIRECTIONS
    }

    @Transient
    private val directions = directionNames.mapNotNull { byName(it) }.ifEmpty { ALL_DIRECTIONS }

    private fun getDirection(random: RandomSource): Direction = directions.random(random)

    override fun internalValidate(blocks: Registry<Block>): Boolean = offset > 0

    override fun transform(state: BlockState, random: RandomSource): BlockState {
        if (!transform) return state

        val direction = getDirection(random)
        var finalState = state

        if (state.hasProperty(FaceAttachedHorizontalDirectionalBlock.FACE))
            finalState = finalState.setValue(
                FaceAttachedHorizontalDirectionalBlock.FACE,
                when (direction) {
                    Direction.UP -> AttachFace.FLOOR
                    Direction.DOWN -> AttachFace.CEILING
                    else -> AttachFace.WALL
                }
            )

        if (state.hasProperty(BlockStateProperties.FACING))
            finalState = finalState.setValue(
                DirectionalBlock.FACING,
                direction.opposite
            )
        else if (direction.axis.isHorizontal)
            finalState = finalState.rotate(
                Rotation.values()[CARDINAL_DIRECTIONS.indexOf(direction) % 4]
            )

        return finalState
    }

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos {
        val direction = getDirection(random)
        return pos.relative(direction, offset)
    }
}