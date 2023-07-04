package com.possible_triangle.skygrid.test.tests

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.extras.Side
import com.possible_triangle.skygrid.api.xml.elements.providers.SingleBlock
import com.possible_triangle.skygrid.test.TestExtension
import com.possible_triangle.skygrid.test.mocks.WorldMock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestExtension::class)
class SideTest {

    private val attached = SingleBlock("stone")
    private val base = { extra: Extra ->
        SingleBlock("dirt", extras = listOf(extra))
    }

    @Test
    fun generatesAbove() {
        val side = Side(Direction.UP.getName(), listOf(attached), offset = 2)
        WorldMock.generate(base(side))

        WorldMock.assert(BlockPos.ZERO, Blocks.DIRT)
        WorldMock.assert(BlockPos.ZERO.above(2), Blocks.STONE)
    }

    @Test
    fun generatesNorth() {
        val side = Side(Direction.NORTH.getName(), listOf(attached), offset = 2)
        WorldMock.generate(base(side))

        WorldMock.assert(BlockPos.ZERO, Blocks.DIRT)
        WorldMock.assert(BlockPos.ZERO.north(2), Blocks.STONE)
    }

    @Test
    fun generatesEast() {
        val side = Side(Direction.EAST.getName(), listOf(attached))
        WorldMock.generate(base(side))

        WorldMock.assert(BlockPos.ZERO, Blocks.DIRT)
        WorldMock.assert(BlockPos.ZERO.east(), Blocks.STONE)
    }

    @Test
    fun generatesSouth() {
        val side = Side(Direction.SOUTH.getName(), listOf(attached), offset = 5)
        WorldMock.generate(base(side))

        WorldMock.assert(BlockPos.ZERO, Blocks.DIRT)
        WorldMock.assert(BlockPos.ZERO.south(5), Blocks.STONE)
    }

    @Test
    fun generatesWest() {
        val side = Side(Direction.WEST.getName(), listOf(attached), offset = 4)
        WorldMock.generate(base(side))

        WorldMock.assert(BlockPos.ZERO, Blocks.DIRT)
        WorldMock.assert(BlockPos.ZERO.west(4), Blocks.STONE)
    }

}