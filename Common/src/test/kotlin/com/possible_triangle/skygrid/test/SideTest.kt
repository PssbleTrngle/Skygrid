package com.possible_triangle.skygrid.test

import com.possible_triangle.skygrid.test.mocks.WorldMock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.Bootstrap
import net.minecraft.world.level.block.Blocks
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.Side
import possible_triangle.skygrid.data.xml.impl.SingleBlock
import kotlin.random.Random

class SideTest {

    companion object {
        @BeforeAll
        fun bootstrapMinecraft() {
            Bootstrap.bootStrap()
        }
    }

    private val random = Random
    private val world = WorldMock()

    private val attached = SingleBlock("stone")
    private val base = { extra: Extra ->
        SingleBlock("dirt", extras = listOf(extra))
    }

    @BeforeEach
    fun resetWorld() {
        world.reset()
    }

    @Test
    fun generatesAbove() {
        val side = Side(Direction.UP.getName(), listOf(attached), offset = 2)
        val config = DimensionConfig(blocks = ListWrapper(base(side)))
        config.generate(random, world)

        world.assert(BlockPos.ZERO, Blocks.DIRT)
        world.assert(BlockPos.ZERO.above(2), Blocks.STONE)
    }

}