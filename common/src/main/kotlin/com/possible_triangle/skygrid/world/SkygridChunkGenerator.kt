package com.possible_triangle.skygrid.world

import com.mojang.datafixers.util.Function5
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.xml.resources.DimensionConfigs
import com.possible_triangle.skygrid.xml.resources.Presets
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.SectionPos
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.tags.BiomeTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.RandomState
import net.minecraft.world.level.levelgen.blending.Blender
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class SkygridChunkGenerator(
    biomeSource: BiomeSource,
    structures: Registry<StructureSet>,
    private val configKey: String,
    private val endPortals: Boolean,
) : ChunkGenerator(structures, Optional.empty(), biomeSource) {

    companion object {
        fun create(
            dimension: ResourceKey<LevelStem>,
            biomeSource: BiomeSource? = null,
        ): ChunkGenerator {
            val biomes = BuiltinRegistries.BIOME
            val structures = BuiltinRegistries.STRUCTURE_SETS
            val config = dimension.location() ?: ResourceLocation(SkygridConstants.MOD_ID, "default")

            return SkygridChunkGenerator(
                biomeSource ?: FixedBiomeSource(biomes.getHolderOrThrow(Biomes.THE_VOID)),
                structures,
                config.toString(),
                dimension == LevelStem.OVERWORLD
            )
        }

        private val BEDROCK = Blocks.BEDROCK.defaultBlockState()

        val CODEC: Codec<SkygridChunkGenerator> = RecordCodecBuilder.create { builder ->
            commonCodec(builder).and(
                builder.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                    Codec.STRING.fieldOf("config").forGetter { it.configKey },
                    Codec.LONG.optionalFieldOf("seed").forGetter { Optional.empty() },
                    Codec.BOOL.optionalFieldOf("endPortals").forGetter { Optional.of(it.endPortals) },
                )
            ).apply(
                builder,
                builder.stable(Function5 { structures, source, key, _, endPortals ->
                    SkygridChunkGenerator(
                        source,
                        structures,
                        key,
                        endPortals.orElse(false),
                    )
                }),
            )
        }
    }

    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    private val config
        get() = DimensionConfigs[ResourceLocation(configKey)] ?: DimensionConfigs.DEFAULT

    private val endPortal
        get() = Presets[ResourceLocation("end_portal")]

    private val strongholdSettings =
        if (endPortals) ConcentricRingsStructurePlacement(
            32, 3, 128,
            BuiltinRegistries.BIOME.getOrCreateTag(BiomeTags.STRONGHOLD_BIASED_TO)
        )
        else null

    private var cachedEndPortalPositions: List<ChunkPos>? = null
    fun endPortalPositions(random: RandomSource): List<ChunkPos> = cachedEndPortalPositions ?: run {
        strongholdSettings?.let {
            val distance = it.distance()
            val count = it.count()
            var spread = it.spread()

            var k = 0
            var j = 0
            var baseDegree = random.nextDouble() * Math.PI * 2.0

            (0 until count).map {
                val degFactor =
                    (4 * distance + distance * k * 6).toDouble() + (random.nextDouble() - 0.5) * distance.toDouble() * 2.5
                var degX = (cos(baseDegree) * degFactor).roundToInt()
                var degZ = (sin(baseDegree) * degFactor).roundToInt()

                degX = SectionPos.blockToSectionCoord(degX)
                degZ = SectionPos.blockToSectionCoord(degZ)

                baseDegree += Math.PI * 2.0 / spread.toDouble()
                ++j

                if (j == spread) {
                    ++k
                    j = 0
                    spread += 2 * spread / (k + 1)
                    spread = spread.coerceAtMost(count - it)
                    baseDegree += random.nextDouble() * Math.PI * 2.0
                }

                ChunkPos(degX, degZ)
            }
        } ?: emptyList()
    }.also {
        cachedEndPortalPositions = it
    }

    private fun RandomState.gridRandom() =
        getOrCreateRandomFactory(ResourceLocation(SkygridConstants.MOD_ID, "generator"))

    private fun RandomState.gridRandom(chunk: ChunkAccess, y: Int = 0) = gridRandom().at(chunk.pos.x, y, chunk.pos.z)

    override fun fillFromNoise(
        executor: Executor,
        blender: Blender,
        randomState: RandomState,
        structures: StructureManager,
        chunk: ChunkAccess,
    ): CompletableFuture<ChunkAccess> {
        val random = randomState.gridRandom(chunk)
        val strongholdRandom = RandomSource.create(randomState.legacyLevelSeed())

        val access = GeneratorBlockAccess(config, chunk)

        val hasEndPortal = endPortalPositions(strongholdRandom).contains(chunk.pos)

        var generatedPortal = false
        for (x in 0 until 16) for (z in 0 until 16) for (y in minY until access.maxY) {

            access.move(x, y, z)
            val isFloor = y == access.minY

            if (access.shouldPlaceBlock()) {
                if (isFloor && endPortal != null && hasEndPortal && (x > 3 && z > 3) && !generatedPortal) {
                    endPortal!!.generate(random, access)
                    generatedPortal = true
                } else if (isFloor) {
                    access.set(BEDROCK)
                } else config.generate(random, access)
            } else {
                access.fillGap()
            }

        }

        return CompletableFuture.completedFuture(chunk)
    }

    override fun buildSurface(
        region: WorldGenRegion,
        structures: StructureManager,
        randomState: RandomState,
        chunk: ChunkAccess,
    ) {
        val random = randomState.gridRandom(chunk, 1)
        val createCeiling = region.dimensionType().hasCeiling()

        val access = GeneratorBlockAccess(config, chunk)
        val startY = access.maxY + (access.maxY % config.distance.y)

        for (x in 0 until 16) for (z in 0 until 16) for (y in startY..startY + config.distance.y) {

            access.move(x, y, z)

            if (y == startY && access.shouldPlaceBlock()) {
                if (createCeiling) {
                    access.set(BEDROCK)
                } else {
                    config.generate(random, access)
                }
            } else {
                access.fillGap()
            }

        }
    }

    /*
    override fun findNearestMapFeature(
        level: ServerLevel,
        structures: HolderSet<ConfiguredStructureFeature<*, *>?>,
        pos: BlockPos,
        something: Int,
        somethingElse: Boolean,
    ): Pair<BlockPos, Holder<ConfiguredStructureFeature<*, *>>>? {
        val distance = config.distance
        val structureTag = structures.unwrap().left().orElse(null)
        return if (structureTag == ConfiguredStructureTags.EYE_OF_ENDER_LOCATED) {
            val pos = endPortalPositions.map {
                BlockPos(it.minBlockX + distance.x,
                    level.minBuildHeight,
                    it.minBlockZ + distance.z)
            }.minByOrNull { it.distSqr(pos) }
            Pair(pos, null)
        } else null
    }
    */

    override fun applyCarvers(
        p0: WorldGenRegion,
        p1: Long,
        p2: RandomState,
        p3: BiomeManager,
        p4: StructureManager,
        p5: ChunkAccess,
        p6: GenerationStep.Carving,
    ) {
        // no carving
    }

    override fun spawnOriginalMobs(region: WorldGenRegion) {
        //TODO maybe?
    }

    override fun getGenDepth(): Int {
        return 384
    }

    override fun getSeaLevel(): Int {
        return -63
    }

    override fun getMinY(): Int {
        return 0
    }

    override fun getBaseHeight(
        x: Int,
        z: Int,
        type: Heightmap.Types,
        accessor: LevelHeightAccessor,
        randomState: RandomState,
    ): Int {
        return min(config.maxY, accessor.maxBuildHeight)
    }

    override fun getBaseColumn(x: Int, z: Int, accessor: LevelHeightAccessor, randomState: RandomState): NoiseColumn {
        return NoiseColumn(minY, emptyArray<BlockState>())
    }

    override fun addDebugScreenInfo(info: MutableList<String>, randomState: RandomState, pos: BlockPos) {
        info.add("Skygrid Config: $configKey")
    }

}