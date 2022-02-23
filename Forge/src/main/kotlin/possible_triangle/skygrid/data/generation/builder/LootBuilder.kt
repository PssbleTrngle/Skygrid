package possible_triangle.skygrid.data.generation.builder

import net.minecraft.resources.ResourceLocation
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.LootTable

class LootBuilder {

    private val tables = arrayListOf<LootTable>()

    fun table(key: ResourceLocation, weight: Double = 1.0) = table(key.toString(), weight)

    fun table(id: String, weight: Double = 1.0) = tables.add(LootTable(id, weight))

    fun build(): ListWrapper<LootTable> {
        return ListWrapper(tables.toList())
    }

}