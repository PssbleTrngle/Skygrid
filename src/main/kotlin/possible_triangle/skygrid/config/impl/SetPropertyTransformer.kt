package possible_triangle.skygrid.config.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import possible_triangle.skygrid.config.PropertyTransformer
import kotlin.random.Random

@Serializable
@SerialName("set")
data class SetPropertyTransformer(override val key: String, val value: String) : PropertyTransformer() {

    override fun <T : Comparable<T>> transform(
        state: BlockState,
        property: Property<T>,
        random: Random,
    ): BlockState {
        return property.getValue(value).map {
            state.setValue(property, it)
        }.orElse(state)
    }

}