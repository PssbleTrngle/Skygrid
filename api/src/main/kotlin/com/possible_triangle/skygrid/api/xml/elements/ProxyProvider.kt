package com.possible_triangle.skygrid.api.xml.elements

import com.possible_triangle.skygrid.api.world.IBlockAccess
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block

abstract class ProxyProvider : BlockProvider() {

    abstract fun get(random: RandomSource): BlockProvider

    final override fun base(random: RandomSource): Block {
        return get(random).base(random)
    }

    override fun generateBase(random: RandomSource, chunk: IBlockAccess): Boolean {
        return get(random).generate(random, chunk)
    }

}