package com.possible_triangle.skygrid.datagen.builder

import net.minecraft.resources.ResourceLocation
import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.impl.LootTable

class LootBuilder {

    private val tables = arrayListOf<LootTable>()

    fun table(key: ResourceLocation, weight: Double = 1.0) = table(key.toString(), weight)

    fun table(id: String, weight: Double = 1.0) = tables.add(LootTable(id, weight))

    fun build(): ListWrapper<LootTable> {
        return ListWrapper(tables.toList())
    }

}