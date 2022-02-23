package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import possible_triangle.skygrid.data.xml.PropertyTransformer
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