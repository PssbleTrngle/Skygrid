package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.datagen.DatagenContext
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.*

class DimensionBuilder(private val createType: (DimensionTypeBuilder.() -> Unit) -> Unit, context: DatagenContext) {

    data class Result(val type: ResourceLocation?, val config: ResourceLocation?, val biomeSource: BiomeSource)

    private val biomeRegistry = context.registries.registry(Registry.BIOME_REGISTRY).orElseThrow()

    var type: ResourceLocation? = null

    var config: ResourceLocation? = null

    var biomeSource: BiomeSource = FixedBiomeSource(biomeRegistry.getHolderOrThrow(Biomes.THE_VOID))

    fun fixedBiomeSource(biome: ResourceKey<Biome>) {
        biomeSource = FixedBiomeSource(biomeRegistry.getHolderOrThrow(biome))
    }

    fun type(builder: DimensionTypeBuilder.() -> Unit) {
        createType(builder)
    }

    fun build() = Result(
        type, config, biomeSource
    )

    fun multipleBiomeSource(vararg biomes: ResourceKey<Biome>) {
        val holders = biomes.mapTo(ArrayList()) {
            biomeRegistry.getHolderOrThrow(it)
        }

        biomeSource = CheckerboardColumnBiomeSource(HolderSet.direct(holders), 10)
    }

}
