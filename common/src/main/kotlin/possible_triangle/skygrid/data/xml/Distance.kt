package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos

@Serializable
@SerialName("distance")
data class Distance(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun of(value: Int): Distance {
            return Distance(value, value, value)
        }

        val DEFAULT = of(4)
    }

    fun isBlock(pos: BlockPos): Boolean {
        return (pos.x % x == 0) && (pos.z % z == 0) && (pos.y % y == 0)
    }
}