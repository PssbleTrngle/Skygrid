package possible_triangle.skygrid.world

import com.mojang.serialization.Lifecycle
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.WorldGenSettings
import net.minecraftforge.common.world.ForgeWorldPreset

@ExperimentalSerializationApi
class SkygridGenerator : ForgeWorldPreset(null) {

    override fun createChunkGenerator(registries: RegistryAccess, seed: Long, settings: String): ChunkGenerator {
        val biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY)
        return SkygridChunkGenerator(
            FixedBiomeSource(biomes.getOrThrow(Biomes.THE_VOID)), seed,
        )
    }

    override fun createSettings(
        registries: RegistryAccess,
        seed: Long,
        structures: Boolean,
        loot: Boolean,
        settings: String,
    ): WorldGenSettings {
        return WorldGenSettings(seed,
            structures,
            loot,
            WorldGenSettings.withOverworld(registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental()),
                createChunkGenerator(registries, seed, settings)))
    }

}