package possible_triangle.skygrid.config.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import possible_triangle.skygrid.config.WeightedEntry

@Serializable
@SerialName("table")
data class LootTable(val id: String, override val weight: Double = 1.0) : WeightedEntry()