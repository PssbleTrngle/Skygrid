package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.datagen.DatagenContext
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.*

class DimensionBuilder(private val createType: (DimensionTypeBuilder.() -> Unit) -> Unit, context: DatagenContext) {

    data class Result(val type: ResourceLocation?, val config: ResourceLocation?, val biomeSource: BiomeSource)

    private val biomeRegistry = context.lookup.get().lookup(Registries.BIOME).orElseThrow()

    var type: ResourceLocation? = null

    var config: ResourceLocation? = null

    var biomeSource: BiomeSource = FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.THE_VOID))

    fun fixedBiomeSource(biome: ResourceKey<Biome>) {
        biomeSource = FixedBiomeSource(biomeRegistry.getOrThrow(biome))
    }

    fun type(builder: DimensionTypeBuilder.() -> Unit) {
        createType(builder)
    }

    fun build() = Result(
        type, config, biomeSource
    )

    fun multipleBiomeSource(vararg biomes: ResourceKey<Biome>) {
        val holders = biomes.mapTo(ArrayList()) {
            biomeRegistry.getOrThrow(it)
        }

        biomeSource = CheckerboardColumnBiomeSource(HolderSet.direct(holders), 10)
    }

}
