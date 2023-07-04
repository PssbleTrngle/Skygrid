package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.SpawnerEntry
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

class MobsBuilder(private val registries: RegistryAccess) {

    private val mobs = arrayListOf<SpawnerEntry>()

    fun mob(type: EntityType<*>, weight: Double = 1.0): Boolean {
        val mobs = registries.registryOrThrow(Registry.ENTITY_TYPE_REGISTRY)
        val key = requireNotNull(mobs.getKey(type))
        return mob(key, weight)
    }

    fun mob(key: ResourceLocation, weight: Double = 1.0) = mob(key.toString(), weight)

    fun mob(id: String, weight: Double = 1.0) = mobs.add(SpawnerEntry(id, weight))

    fun build(): ListWrapper<SpawnerEntry> {
        return ListWrapper(mobs.toList())
    }

}