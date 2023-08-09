package com.possible_triangle.skygrid.api.xml.elements.transformers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import com.possible_triangle.skygrid.api.xml.elements.PropertyTransformer
import net.minecraft.util.RandomSource

@Serializable
@SerialName("set")
data class SetPropertyTransformer(override val key: String, val value: String) : PropertyTransformer() {

    override fun <T : Comparable<T>> transform(
        state: BlockState,
        property: Property<T>,
        random: RandomSource,
    ): BlockState {
        return property.getValue(value).map {
            state.setValue(property, it)
        }.orElse(state)
    }

}