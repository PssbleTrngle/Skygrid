package possible_triangle.skygrid.config.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import possible_triangle.skygrid.config.Transformer
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
@SerialName("property")
data class PropertyTransformer(val key: String, val value: String) : Transformer() {

    override fun apply(state: BlockState, random: Random): BlockState {
        val property = state.properties.find { it.name == value } ?: return state
        return transform(state, property)
    }

    private fun <T : Comparable<T>> transform(
        state: BlockState,
        property: Property<T>,
    ): BlockState {
        return property.getValue(value).map {
            state.setValue(property, it)
        }.orElse(state)
    }

}