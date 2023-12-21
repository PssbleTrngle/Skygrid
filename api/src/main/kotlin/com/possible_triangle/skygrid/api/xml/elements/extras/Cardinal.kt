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

@Serializable
@SerialName("surround")
class Cardinal(
    override val providers: List<BlockProvider>,
    val offset: Int = 1,
    val transform: Boolean = true,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
    val directions: List<Direction> = listOf(NORTH, SOUTH, EAST, WEST)
) : Extra() {

    private fun getDirection(random: RandomSource): Direction = directions.random(random)

    override fun internalValidate(blocks: Registry<Block>): Boolean = offset > 0

    override fun transform(state: BlockState, random: RandomSource): BlockState {
        if (!transform) return state
        val direction = getDirection(random)
        if (state.hasProperty(FaceAttachedHorizontalDirectionalBlock.FACE))
            state.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, when(direction) {
                UP -> CEILING
                DOWN -> FLOOR
                else -> WALL
            })
        if (state.hasProperty(DirectionalBlock.FACING))
            state.setValue(DirectionalBlock.FACING, direction)
        if (direction.axis.isVertical) state.rotate(Rotation.values()[random.nextInt(4)])
        return state
    }

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos =
        pos.relative(getDirection(random), offset)
}