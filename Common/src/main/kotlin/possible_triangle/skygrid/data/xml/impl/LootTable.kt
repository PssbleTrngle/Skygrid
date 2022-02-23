package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import possible_triangle.skygrid.data.xml.WeightedEntry

@Serializable
@SerialName("table")
data class LootTable(val id: String, override val weight: Double = 1.0) : WeightedEntry()