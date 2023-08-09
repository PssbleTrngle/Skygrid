package com.possible_triangle.skygrid.api.xml.elements.transformers

import com.possible_triangle.skygrid.api.extensions.random
import com.possible_triangle.skygrid.api.xml.elements.PropertyTransformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

@Serializable
@SerialName("cycle")
data class CyclePropertyTransformer(override val key: String) : PropertyTransformer() {

    override fun <T : Comparable<T>> transform(
        state: BlockState,
        property: Property<T>,
        random: RandomSource,
    ): BlockState {
        val value = property.possibleValues.random(random)
        return state.setValue(property, value)
    }

}