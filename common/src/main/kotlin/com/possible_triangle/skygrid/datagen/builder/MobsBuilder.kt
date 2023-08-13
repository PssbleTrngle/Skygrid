package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.SpawnerEntry
import com.possible_triangle.skygrid.datagen.DatagenContext
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

class MobsBuilder(private val context: DatagenContext) {

    private val mobs = arrayListOf<SpawnerEntry>()

    fun mob(type: EntityType<*>, weight: Double = 1.0): Boolean {
        val mobs = context.registries.registryOrThrow(Registry.ENTITY_TYPE_REGISTRY)
        val key = requireNotNull(mobs.getKey(type))
        return mob(key, weight)
    }

    fun mob(key: ResourceLocation, weight: Double = 1.0) = mobs.add(SpawnerEntry(key.toString(), weight))

    fun mob(id: String, mod: String = context.defaultMod, weight: Double = 1.0) = mob(ResourceLocation(mod, id), weight)

    fun build(): ListWrapper<SpawnerEntry> {
        return ListWrapper(mobs.toList())
    }

}