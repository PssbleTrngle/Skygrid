package possible_triangle.skygrid.world

import net.minecraft.client.gui.screens.worldselection.WorldPreset
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.dedicated.DedicatedServerProperties
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.WorldGenSettings
import possible_triangle.skygrid.SkygridMod.MOD_ID
import kotlin.random.Random

object SkygridGenerator : WorldPreset("${MOD_ID}.${MOD_ID}") {

    fun fromServerProperties(registries: RegistryAccess, properties: DedicatedServerProperties.WorldGenProperties): WorldGenSettings? {
        val levelType = properties.levelType
        if (levelType != MOD_ID) return null

        val seed = properties.levelSeed.takeIf { it.isNotEmpty() }?.let {
            try {
                it.toLong()
            } catch (ex: NumberFormatException) {
                it.hashCode().toLong()
            }
        }.takeIf { it != 0L } ?: Random.nextLong()

        return SkygridChunkGenerator.createSettings(registries, seed, false)
    }

    override fun generator(registries: RegistryAccess, seed: Long): ChunkGenerator {
        val biomes = registries.registryOrThrow(
            Registry.BIOME_REGISTRY)
        val structures = registries.registryOrThrow(
            Registry.STRUCTURE_SET_REGISTRY)
        val config = ResourceLocation(MOD_ID, "default")
        return SkygridChunkGenerator(
            FixedBiomeSource(biomes.getHolderOrThrow(Biomes.THE_VOID)), structures, config.toString(), seed, false
        )
    }

    override fun create(
        registries: RegistryAccess,
        seed: Long,
        generateFeatures: Boolean,
        bonusChest: Boolean,
    ): WorldGenSettings = SkygridChunkGenerator.createSettings(registries, seed, bonusChest)
}