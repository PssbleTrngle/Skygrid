package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.LootTable
import com.possible_triangle.skygrid.datagen.DatagenContext
import net.minecraft.resources.ResourceLocation

class LootBuilder(private val context: DatagenContext) {

    private val tables = arrayListOf<LootTable>()

    fun table(key: ResourceLocation, weight: Double = 1.0) = tables.add(LootTable(key.toString(), weight))

    fun table(id: String, mod: String = context.defaultMod, weight: Double = 1.0) =
        table(ResourceLocation(mod, id), weight)

    fun build(): ListWrapper<LootTable> {
        return ListWrapper(tables.toList())
    }

}