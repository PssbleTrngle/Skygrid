package possible_triangle.skygrid.world

import com.mojang.serialization.Lifecycle
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.WorldGenSettings
import net.minecraftforge.common.world.ForgeWorldPreset

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
        generateStructures: Boolean,
        bonusChest: Boolean,
        generatorSettings: String,
    ): WorldGenSettings {
        val dimensions = MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental())

        val generator = createChunkGenerator(registries, seed, generatorSettings)
        dimensions.register(LevelStem.OVERWORLD, LevelStem({
            registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrThrow(DimensionType.OVERWORLD_LOCATION)
        }, generator), Lifecycle.stable())

        DimensionType.defaultDimensions(registries, seed).entrySet().forEach { (key, stem) ->
            dimensions.register(key, LevelStem(stem.typeSupplier(), generator), Lifecycle.stable())
        }

        return WorldGenSettings(seed, false, bonusChest, dimensions)
    }

}