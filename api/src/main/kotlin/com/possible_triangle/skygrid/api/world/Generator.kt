package com.possible_triangle.skygrid.api.world

import net.minecraft.util.RandomSource

fun interface Generator<B : IBlockAccess> {
    fun generate(random: RandomSource, access: B): Boolean
}