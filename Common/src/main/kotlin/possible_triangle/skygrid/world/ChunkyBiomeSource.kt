package possible_triangle.skygrid.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import java.util.function.BiFunction

data class ChunkyBiomeSource(private val biomes: List<Biome>, private val seed: Long) : BiomeSource(biomes) {

    companion object {
        val CODEC: Codec<ChunkyBiomeSource> = RecordCodecBuilder.create { builder ->
            builder.group(
                ExtraCodecs.nonEmptyList(Biome.DIRECT_CODEC.listOf()).fieldOf("biomes").forGetter { it.biomes },
                Codec.LONG.fieldOf("seed").forGetter { it.seed },
            ).apply(builder, builder.stable(BiFunction(::ChunkyBiomeSource)))
        }
    }

    enum class Preset(private val biomes: List<ResourceKey<Biome>>) {
        OVERWORLD(listOf(Biomes.PLAINS,
            Biomes.BAMBOO_JUNGLE,
            Biomes.BIRCH_FOREST,
            Biomes.BEACH,
            Biomes.BADLANDS,
            Biomes.DESERT,
            Biomes.WARM_OCEAN,
            Biomes.COLD_OCEAN,
            Biomes.FLOWER_FOREST,
            Biomes.FOREST,
            Biomes.ICE_SPIKES,
            Biomes.JUNGLE,
            Biomes.JAGGED_PEAKS,
            Biomes.DARK_FOREST)),

        NETHER(listOf(Biomes.NETHER_WASTES,
            Biomes.BASALT_DELTAS,
            Biomes.SOUL_SAND_VALLEY,
            Biomes.CRIMSON_FOREST,
            Biomes.WARPED_FOREST));

        fun biomes(registry: Registry<Biome>) = biomes.map { registry.getOrThrow(it) }
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int, climate: Climate.Sampler): Biome {
        return biomes.random()
    }

    override fun codec(): Codec<out BiomeSource> {
        return CODEC
    }

    override fun withSeed(seed: Long): BiomeSource {
        return copy(seed = seed)
    }

}