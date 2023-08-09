package com.possible_triangle.skygrid.extensions

import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.XoroshiroRandomSource
import kotlin.random.Random

class SourcedRandom(private val source: RandomSource) : Random {

    override fun nextBits(bitCount: Int) = when(source)  {
        is XoroshiroRandomSource ->  source.nextBits
    }



}