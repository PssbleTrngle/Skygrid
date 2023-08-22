package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.datagen.DatagenContext
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource

class DimensionBuilder(private val createType: (DimensionTypeBuilder.() -> Unit) -> Unit, context: DatagenContext) {

    data class Result(val type: ResourceLocation?, val config: ResourceLocation?, val biomeSource: BiomeSource)

    private val biomes = context.registries.registry(Registry.BIOME_REGISTRY).orElseThrow()

    var type: ResourceLocation? = null

    var config: ResourceLocation? = null

    var biomeSource: BiomeSource = FixedBiomeSource(biomes.getHolderOrThrow(Biomes.THE_VOID))

    fun fixedBiomeSource(biome: ResourceKey<Biome>) {
        biomeSource = FixedBiomeSource(biomes.getHolderOrThrow(biome))
    }

    fun type(builder: DimensionTypeBuilder.() -> Unit) {
        createType(builder)
    }

    fun build() = Result(
        type, config, biomeSource
    )

}
