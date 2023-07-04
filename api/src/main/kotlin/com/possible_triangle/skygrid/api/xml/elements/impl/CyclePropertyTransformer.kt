package com.possible_triangle.skygrid.api.xml.elements.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import com.possible_triangle.skygrid.api.xml.elements.PropertyTransformer
import kotlin.random.Random

@Serializable
@SerialName("cycle")
data class CyclePropertyTransformer(override val key: String) : PropertyTransformer() {

    override fun <T : Comparable<T>> transform(
        state: BlockState,
        property: Property<T>,
        random: Random,
    ): BlockState {
        val value = property.possibleValues.random(random)
        return state.setValue(property, value)
    }

}