package com.possible_triangle.skygrid.api.extensions

import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block

fun Collection<BlockProvider>.flat(): List<Pair<Block, Double>> {
    val totalWeight = sumOf { it.weight }
    return flatMap { provider ->
        val probability = provider.weight / totalWeight
        val extras = provider.validExtras.flatMap { extra ->
            extra.validProviders.flat().map { it.first to it.second * extra.probability * probability }
        }
        extras + provider.flat().map { it.first to it.second * probability }
    }
}

fun <T> Collection<T>.random(random: RandomSource): T {
    if (isEmpty()) throw NoSuchElementException("Collection is empty.")
    return elementAt(random.nextInt(size))
}