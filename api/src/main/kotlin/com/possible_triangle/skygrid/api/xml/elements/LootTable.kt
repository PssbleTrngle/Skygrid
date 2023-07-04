package com.possible_triangle.skygrid.api.xml.elements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("table")
data class LootTable(val id: String, override val weight: Double = 1.0) : WeightedEntry()