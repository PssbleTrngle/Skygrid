package com.possible_triangle.skygrid.world

import com.google.common.collect.ImmutableMap
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.presets.WorldPreset

class SkygridPreset : WorldPreset(createDimensions()) {

    companion object {
        private fun createDimensions(): Map<ResourceKey<LevelStem>, LevelStem> {
            val biomes = BuiltinRegistries.BIOME
            val builder = ImmutableMap.Builder<ResourceKey<LevelStem>,LevelStem>()

            fun register(
                stem: ResourceKey<LevelStem>,
                dimension: ResourceKey<DimensionType>,
                biomeSource: BiomeSource,
            ) {
                val generator = SkygridChunkGenerator.create(stem, biomeSource)
                builder.put(stem, LevelStem(
                    BuiltinRegistries.DIMENSION_TYPE.getHolderOrThrow(dimension),
                    generator))
            }

            register(
                LevelStem.OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD,
                FixedBiomeSource(biomes.getHolderOrThrow(Biomes.PLAINS))
            )

            register(
                LevelStem.NETHER,
                BuiltinDimensionTypes.NETHER,
                FixedBiomeSource(biomes.getHolderOrThrow(Biomes.NETHER_WASTES))
            )

            register(
                LevelStem.END,
                BuiltinDimensionTypes.END,
                FixedBiomeSource(biomes.getHolderOrThrow(Biomes.THE_END))
            )

            return builder.build()
        }
    }

}