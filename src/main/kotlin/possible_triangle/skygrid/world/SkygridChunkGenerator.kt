package possible_triangle.skygrid.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureFeatureManager
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.StructureSettings
import net.minecraft.world.level.levelgen.blending.Blender
import possible_triangle.skygrid.config.impl.DimensionConfig
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.BiFunction
import kotlin.random.Random

@ExperimentalSerializationApi
class SkygridChunkGenerator(
    biomeSource: BiomeSource,
    private val seed: Long,
) : ChunkGenerator(biomeSource, biomeSource, StructureSettings(false), seed) {

    companion object {

        private val CLIMATE = Climate.Sampler { _, _, _ -> Climate.TargetPoint(1L, 1L, 1L, 1L, 1L, 1L) }

        private val CODEC: Codec<SkygridChunkGenerator> = RecordCodecBuilder.create { builder ->
            builder.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                Codec.LONG.fieldOf("seed").stable().forGetter { it.seed }).apply(
                builder,
                builder.stable(BiFunction { source, seed ->
                    SkygridChunkGenerator(source, seed)
                }),
            )
        }
    }

    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun withSeed(seed: Long): SkygridChunkGenerator {
        return SkygridChunkGenerator(biomeSource, seed)
    }

    private val random = Random(seed)

    override fun buildSurface(region: WorldGenRegion, structures: StructureFeatureManager, chunk: ChunkAccess) {
        val dimensions = region.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY)
        val config = dimensions.getKey(region.dimensionType())?.let { DimensionConfig[it] } ?: DimensionConfig.DEFAULT

        val margin = BlockPos(config.margin, config.margin, config.margin)
        val blocks = region.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY)
        for (x in 0..16) if (x % margin.x == 0)
            for (z in 0..16) if (z % margin.z == 0)
                for (y in chunk.minBuildHeight..chunk.maxBuildHeight) if (y % margin.y == 0) {
                    val pos = BlockPos(x, y, z)
                    config.blocks.generate(random, chunk, blocks, pos)
                }
    }

    override fun climateSampler(): Climate.Sampler {
        return CLIMATE
    }

    override fun applyCarvers(
        region: WorldGenRegion,
        seed: Long,
        biomes: BiomeManager,
        structures: StructureFeatureManager,
        chunk: ChunkAccess,
        step: GenerationStep.Carving,
    ) {

    }

    override fun spawnOriginalMobs(region: WorldGenRegion) {}

    override fun getGenDepth(): Int {
        return 1
    }

    override fun fillFromNoise(
        executor: Executor,
        blender: Blender,
        structures: StructureFeatureManager,
        chunk: ChunkAccess,
    ): CompletableFuture<ChunkAccess> {
        return CompletableFuture.completedFuture(chunk)
    }

    override fun getSeaLevel(): Int {
        return 0
    }

    override fun getMinY(): Int {
        return 0
    }

    override fun getBaseHeight(
        x: Int,
        z: Int,
        type: Heightmap.Types,
        accessor: LevelHeightAccessor,
    ): Int {
        return 0
    }

    override fun getBaseColumn(x: Int, z: Int, accessor: LevelHeightAccessor): NoiseColumn {
        return NoiseColumn(minY, emptyArray<BlockState>())
    }
}