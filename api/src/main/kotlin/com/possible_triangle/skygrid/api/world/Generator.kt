package com.possible_triangle.skygrid.api.world

import kotlin.random.Random

fun interface Generator<B : IBlockAccess> {
    fun generate(random: Random, access: B): Boolean
}