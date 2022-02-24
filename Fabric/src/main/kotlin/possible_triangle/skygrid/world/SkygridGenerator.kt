package possible_triangle.skygrid.world

import net.minecraft.client.gui.screens.worldselection.WorldPreset
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.WorldGenSettings
import possible_triangle.skygrid.SkygridMod.MOD_ID
import java.util.*
import kotlin.random.Random

object SkygridGenerator : WorldPreset("${MOD_ID}.${MOD_ID}") {

    fun fromServerProperties(registries: RegistryAccess, properties: Properties): WorldGenSettings? {
        val levelType = properties.getProperty("level-type")
        if (levelType != MOD_ID) return null

        val seedString = properties.getProperty("level-seed")

        val seed = properties.getProperty("level-seed").takeIf { it.isNotEmpty() }.let {
            try {
                seedString.toLong()
            } catch (ex: NumberFormatException) {
                it.hashCode().toLong()
            }
        }.takeIf { it != 0L } ?: Random.nextLong()

        return SkygridChunkGenerator.createSettings(registries, seed, false)
    }

    override fun generator(registries: RegistryAccess, seed: Long): ChunkGenerator {
        val biomes = registries.registryOrThrow(
            Registry.BIOME_REGISTRY)
        val config = ResourceLocation(MOD_ID, "default")
        return SkygridChunkGenerator(
            FixedBiomeSource(biomes.getOrThrow(Biomes.THE_VOID)), config.toString(), seed,
        )
    }

    override fun create(
        registries: RegistryAccess.RegistryHolder,
        seed: Long,
        generateFeatures: Boolean,
        bonusChest: Boolean,
    ): WorldGenSettings = SkygridChunkGenerator.createSettings(registries, seed, bonusChest)
}