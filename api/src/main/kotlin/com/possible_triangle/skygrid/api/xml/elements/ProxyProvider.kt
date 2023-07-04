package com.possible_triangle.skygrid.api.xml.elements

import com.possible_triangle.skygrid.api.world.IBlockAccess
import net.minecraft.world.level.block.Block
import kotlin.random.Random

abstract class ProxyProvider : BlockProvider() {

    abstract fun get(random: Random): BlockProvider

    final override fun base(random: Random): Block {
        return get(random).base(random)
    }

    override fun generateBase(random: Random, chunk: IBlockAccess): Boolean {
        return get(random).generate(random, chunk, )
    }

}