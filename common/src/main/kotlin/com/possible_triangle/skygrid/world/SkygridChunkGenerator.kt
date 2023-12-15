package com.possible_triangle.skygrid.world

import com.mojang.datafixers.util.Function3
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.xml.resources.GridConfigs
import com.possible_triangle.skygrid.xml.resources.Presets
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.SectionPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.tags.StructureTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.RandomState
import net.minecraft.world.level.levelgen.blending.Blender
import net.minecraft.world.level.levelgen.structure.Structure
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

class SkygridChunkGenerator(
    biomeSource: BiomeSource,
    private val configKey: String,
    private val endPortals: Boolean,
) : ChunkGenerator(biomeSource) {

    companion object {
        private val BEDROCK = Blocks.BEDROCK.defaultBlockState()

        val CODEC: Codec<SkygridChunkGenerator> = RecordCodecBuilder.create { builder ->
            builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                Codec.STRING.fieldOf("config").forGetter { it.configKey },
                Codec.BOOL.optionalFieldOf("endPortals").forGetter { Optional.of(it.endPortals) },
            ).apply(
                builder,
                builder.stable(Function3 { source, key, endPortals ->
                    SkygridChunkGenerator(
                        source,
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
        get() = GridConfigs[ResourceLocation(configKey)] ?: GridConfigs.DEFAULT

    private val endPortal
        get() = Presets[ResourceLocation("end_portal")]

    private var cachedEndPortalPositions: List<ChunkPos>? = null
    fun endPortalPositions(random: RandomSource): List<ChunkPos> = cachedEndPortalPositions ?: run {
        val distance = 32
        val count = 128
        var spread = 3

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
        val strongholdRandom = randomState.gridRandom().at(0, 0, 0)

        val access = GeneratorBlockAccess(config, chunk)

        val hasEndPortal = endPortalPositions(strongholdRandom).contains(chunk.pos)

        var generatedPortal = false
        for (x in 0 until 16) for (z in 0 until 16) for (y in access.minY until access.maxY) {

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

    override fun findNearestMapStructure(
        level: ServerLevel,
        structures: HolderSet<Structure>,
        pos: BlockPos,
        attempts: Int,
        something: Boolean,
    ): Pair<BlockPos, Holder<Structure>>? {
        val match = structures.find { it.`is`(StructureTags.EYE_OF_ENDER_LOCATED) }
        val distance = config.distance
        return if (match != null && cachedEndPortalPositions != null) {
            Pair(cachedEndPortalPositions!!.map {
                BlockPos(
                    it.minBlockX + distance.x,
                    level.minBuildHeight,
                    it.minBlockZ + distance.z
                )
            }.minByOrNull { it.distSqr(pos) }, match)
        } else null
    }

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