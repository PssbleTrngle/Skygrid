package possible_triangle.skygrid.builder

import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.SpawnerEntry

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