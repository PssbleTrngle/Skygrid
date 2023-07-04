package com.possible_triangle.skygrid.api.xml.elements.impl

import com.possible_triangle.skygrid.api.xml.elements.WeightedEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("table")
data class LootTable(val id: String, override val weight: Double = 1.0) : WeightedEntry()