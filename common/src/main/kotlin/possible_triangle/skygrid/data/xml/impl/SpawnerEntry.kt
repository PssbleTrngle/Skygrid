package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.SpawnData
import possible_triangle.skygrid.data.xml.WeightedEntry
import java.util.*

@Serializable
@SerialName("mob")
data class SpawnerEntry(val id: String, override val weight: Double = 1.0) : WeightedEntry() {

    val key
        get() = ResourceLocation(id)

    fun createSpawnData(): SpawnData {
        val nbt = CompoundTag()
        nbt.putString("id", id)
        return SpawnData(nbt, Optional.empty())
    }

}