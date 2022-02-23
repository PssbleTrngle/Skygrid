package possible_triangle.skygrid.world

import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureFeatureManager
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.StructureSettings
import net.minecraft.world.level.levelgen.blending.Blender
import possible_triangle.skygrid.data.xml.DimensionConfig
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class SkygridChunkGenerator(
    biomeSource: BiomeSource,
    private val configKey: String,
    private val seed: Long,
) : ChunkGenerator(biomeSource, biomeSource, StructureSettings(false), seed) {

    companion object {

        private val CLIMATE = Climate.Sampler { _, _, _ -> Climate.TargetPoint(1L, 1L, 1L, 1L, 1L, 1L) }

        private val BEDROCK = Blocks.BEDROCK.defaultBlockState()

        val CODEC: Codec<SkygridChunkGenerator> = RecordCodecBuilder.create { builder ->
            builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                Codec.STRING.fieldOf("config").forGetter { it.configKey },
                Codec.LONG.fieldOf("seed").stable().forGetter { it.seed }
            ).apply(
                builder,
                builder.stable(Function3 { source, key, seed ->
                    SkygridChunkGenerator(source, key, seed)
                }),
            )
        }
    }

    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun withSeed(seed: Long): SkygridChunkGenerator {
        return SkygridChunkGenerator(biomeSource, configKey, seed)
    }

    private val random = Random(seed)

    override fun buildSurface(region: WorldGenRegion, structures: StructureFeatureManager, chunk: ChunkAccess) {
        val config = DimensionConfig[ResourceLocation(configKey)] ?: DimensionConfig.DEFAULT

        val minY = max(chunk.minBuildHeight, config.minY)
        val maxY = min(chunk.maxBuildHeight, config.maxY)

        region.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY)
        for (x in 0 until 16) if ((x + chunk.pos.minBlockX) % config.distance.x == 0)
            for (z in 0 until 16) if ((z + chunk.pos.minBlockZ) % config.distance.z == 0)
                for (y in minY..maxY) if (y % config.distance.y == 0) {
                    val pos = BlockPos(x, y, z)
                    if (y == minY) chunk.setBlockState(pos, BEDROCK, false)
                    else config.generate(random, BlockAccess({ state, offset ->
                        val at = pos.offset(offset)
                        chunk.setBlockState(at, state, false)
                    }, { chunk.getBlockState(pos.offset(it)) }))
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