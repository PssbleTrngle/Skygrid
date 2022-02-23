package possible_triangle.skygrid.data.generation.builder

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.SpawnerEntry

class MobsBuilder {

    private val mobs = arrayListOf<SpawnerEntry>()

    fun mob(type: EntityType<*>, weight: Double = 1.0) = mob(requireNotNull(type.registryName), weight)

    fun mob(key: ResourceLocation, weight: Double = 1.0) = mob(key.toString(), weight)

    fun mob(id: String, weight: Double = 1.0) = mobs.add(SpawnerEntry(id, weight))

    fun build(): ListWrapper<SpawnerEntry> {
        return ListWrapper(mobs.toList())
    }

}