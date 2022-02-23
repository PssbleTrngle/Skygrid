package possible_triangle.skygrid.world

import com.mojang.serialization.Lifecycle
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.Registry.DIMENSION_TYPE_REGISTRY
import net.minecraft.core.Registry.LEVEL_STEM_REGISTRY
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.WorldGenSettings
import net.minecraftforge.common.world.ForgeWorldPreset
import possible_triangle.skygrid.Constants

class SkygridGenerator : ForgeWorldPreset(null) {

    override fun createChunkGenerator(registries: RegistryAccess, seed: Long, settings: String): ChunkGenerator {
        return createChunkGenerator(registries, seed, null)
    }

    private fun createChunkGenerator(
        registries: RegistryAccess,
        seed: Long,
        dimension: ResourceKey<LevelStem>?,
    ): ChunkGenerator {
        val biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY)
        val config = dimension?.location() ?: ResourceLocation(Constants.MOD_ID, "default")
        return SkygridChunkGenerator(
            FixedBiomeSource(biomes.getOrThrow(Biomes.THE_VOID)), config.toString(), seed,
        )
    }

    override fun createSettings(
        registries: RegistryAccess,
        seed: Long,
        generateStructures: Boolean,
        bonusChest: Boolean,
        generatorSettings: String,
    ): WorldGenSettings {
        val dimensions = MappedRegistry(LEVEL_STEM_REGISTRY, Lifecycle.experimental())

        val overworldGenerator = createChunkGenerator(registries, seed, LevelStem.OVERWORLD)
        dimensions.register(LevelStem.OVERWORLD, LevelStem({
            registries.registryOrThrow(DIMENSION_TYPE_REGISTRY).getOrThrow(DimensionType.OVERWORLD_LOCATION)
        }, overworldGenerator), Lifecycle.stable())

        DimensionType.defaultDimensions(registries, seed).entrySet().forEach { (key, stem) ->
            val generator = createChunkGenerator(registries, seed, key)
            dimensions.register(key, LevelStem(stem.typeSupplier(), generator), Lifecycle.stable())
        }

        return WorldGenSettings(seed, false, bonusChest, dimensions)
    }

}