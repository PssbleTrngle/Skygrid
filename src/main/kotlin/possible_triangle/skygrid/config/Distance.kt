package possible_triangle.skygrid.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("distance")
data class Distance(val x:  Int, val y: Int, val z: Int)