package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("distance")
data class Distance(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun of(value: Int): Distance {
            return Distance(value, value, value)
        }

        val DEFAULT = of(4)
    }
}