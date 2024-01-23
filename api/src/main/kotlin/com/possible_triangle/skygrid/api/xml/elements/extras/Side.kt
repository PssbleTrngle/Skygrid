package com.possible_triangle.skygrid.api.xml.elements.extras

import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.level.block.state.properties.BlockStateProperties

@Serializable
@SerialName("side")
data class Side(
    val on: String,
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
    val doTransform: Boolean = false
) : Extra() {

    override fun internalValidate(blocks: Registry<Block>): Boolean =
        Direction.byName(on) != null && offset > 0

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos =
        pos.relative(
            Direction.byName(on) ?: throw NullPointerException("Unknown direction '$on'"),
            offset
        )

    override fun transform(state: BlockState, random: RandomSource): BlockState {
        if (!doTransform) return state

        val direction = Direction.byName(on) ?: throw NullPointerException("Unknown direction '$on'")
        var finalState = state

        if (state.hasProperty(FaceAttachedHorizontalDirectionalBlock.FACE))
            finalState = finalState.setValue(
                FaceAttachedHorizontalDirectionalBlock.FACE, when (direction) {
                    Direction.UP -> AttachFace.FLOOR
                    Direction.DOWN -> AttachFace.CEILING
                    else -> AttachFace.WALL
                }
            )

        if (state.hasProperty(BlockStateProperties.FACING))
            finalState = finalState.setValue(BlockStateProperties.FACING, direction)
        else if (direction.axis.isHorizontal)
            finalState = finalState.rotate(
                Rotation.values()[Cardinal.DIRECTIONS.indexOf(direction) % 4]
            )

        return finalState
    }
}