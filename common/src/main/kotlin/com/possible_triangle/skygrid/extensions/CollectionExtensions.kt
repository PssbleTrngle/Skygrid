package com.possible_triangle.skygrid.extensions

import com.possible_triangle.skygrid.api.extensions.flat
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import net.minecraft.world.level.block.Block

fun Collection<BlockProvider>.weights(): Map<Block, Double> {
    val flat = this.flat()
    return hashMapOf<Block, Double>().apply {
        flat.forEach { (block, weight) ->
            put(block, getOrDefault(block, 0.0) + weight)
        }
    }
}