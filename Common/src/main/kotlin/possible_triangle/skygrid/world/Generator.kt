package possible_triangle.skygrid.world

import kotlin.random.Random

fun interface Generator<B : IBlockAccess> {
    fun generate(random: Random, access: B)
}