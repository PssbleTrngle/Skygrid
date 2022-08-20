package possible_triangle.skygrid.world

import net.minecraft.core.RegistryAccess
import net.minecraft.server.dedicated.DedicatedServerProperties
import net.minecraft.world.level.levelgen.WorldGenSettings
import possible_triangle.skygrid.SkygridMod
import kotlin.random.Random

object ServerProvider {

    fun fromServerProperties(registries: RegistryAccess, properties: DedicatedServerProperties.WorldGenProperties): WorldGenSettings? {
        val levelType = properties.levelType
        if (levelType != SkygridMod.MOD_ID) return null

        val seed = properties.levelSeed.takeIf { it.isNotEmpty() }?.let {
            try {
                it.toLong()
            } catch (ex: NumberFormatException) {
                it.hashCode().toLong()
            }
        }.takeIf { it != 0L } ?: Random.nextLong()

        return SkygridChunkGenerator.createSettings(registries, seed, false)
    }

}