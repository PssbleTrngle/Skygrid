package com.possible_triangle.skygrid.api.xml.elements

import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

abstract class PropertyTransformer : Transformer() {

    abstract val key: String

    final override fun apply(state: BlockState, random: RandomSource): BlockState {
        val property = state.properties.find { it.name == key } ?: return state
        return transform(state, property, random)
    }

    protected abstract fun <T : Comparable<T>> transform(
        state: BlockState,
        property: Property<T>,
        random: RandomSource,
    ): BlockState

}