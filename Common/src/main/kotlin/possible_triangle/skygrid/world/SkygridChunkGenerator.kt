package possible_triangle.skygrid.world

import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureFeatureManager
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.StructureSettings
import net.minecraft.world.level.levelgen.WorldGenSettings
import net.minecraft.world.level.levelgen.blending.Blender
import net.minecraft.world.level.levelgen.feature.StructureFeature
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Preset
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.*
import kotlin.random.Random

class SkygridChunkGenerator(
    biomeSource: BiomeSource,
    private val configKey: String,
    private val seed: Long?,
    private val endPortals: Boolean,
) : ChunkGenerator(biomeSource, StructureSettings(endPortals)) {

    companion object {
        fun create(
            registries: RegistryAccess,
            seed: Long,
            dimension: ResourceKey<LevelStem>?,
        ): ChunkGenerator {
            val biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY)
            val config = dimension?.location() ?: ResourceLocation(SkygridMod.MOD_ID, "default")
            return SkygridChunkGenerator(
                FixedBiomeSource(biomes.getOrThrow(Biomes.THE_VOID)),
                config.toString(),
                seed,
                dimension == LevelStem.OVERWORLD
            )
        }

        fun createSettings(
            registries: RegistryAccess,
            seed: Long,
            bonusChest: Boolean,
        ): WorldGenSettings {
            val dimensions = MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental())

            val overworldGenerator = create(registries, seed, LevelStem.OVERWORLD)
            dimensions.register(LevelStem.OVERWORLD, LevelStem({
                registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY)
                    .getOrThrow(DimensionType.OVERWORLD_LOCATION)
            }, overworldGenerator), Lifecycle.stable())

            DimensionType.defaultDimensions(registries, seed).entrySet().forEach { (key, stem) ->
                val generator = create(registries, seed, key)
                dimensions.register(key, LevelStem(stem.typeSupplier(), generator), Lifecycle.stable())
            }

            return WorldGenSettings(seed, false, bonusChest, dimensions)
        }

        private val CLIMATE = Climate.Sampler { _, _, _ -> Climate.TargetPoint(1L, 1L, 1L, 1L, 1L, 1L) }

        private val BEDROCK = Blocks.BEDROCK.defaultBlockState()

        val CODEC: Codec<SkygridChunkGenerator> = RecordCodecBuilder.create { builder ->
            builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                Codec.STRING.fieldOf("config").forGetter { it.configKey },
                Codec.LONG.optionalFieldOf("seed").forGetter { Optional.ofNullable(it.seed) },
                Codec.BOOL.optionalFieldOf("endPortals").forGetter { Optional.of(it.endPortals) },
            ).apply(
                builder,
                builder.stable(Function4 { source, key, seed, endPortals ->
                    SkygridChunkGenerator(source, key, seed.orElse(null), endPortals.orElse(false))
                }),
            )
        }
    }

    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun withSeed(seed: Long): SkygridChunkGenerator {
        return SkygridChunkGenerator(biomeSource, configKey, seed, endPortals)
    }

    private val random = seed?.let { Random(it) } ?: Random
    private val strongholdRandom = Random(random.nextLong())

    private val config
        get() = DimensionConfig[ResourceLocation(configKey)] ?: DimensionConfig.DEFAULT

    private val endPortal
        get() = Preset[ResourceLocation("end_portal")]

    private val endPortalPositions: List<ChunkPos> = settings.stronghold()?.let {
        val distance = it.distance()
        val count = it.count()
        var spread = it.spread()

        var k = 0
        var j = 0
        var baseDegree = strongholdRandom.nextDouble() * Math.PI * 2.0

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

    override fun fillFromNoise(
        executor: Executor,
        blender: Blender,
        structures: StructureFeatureManager,
        chunk: ChunkAccess,
    ): CompletableFuture<ChunkAccess> {
        val config = config
        val endPortal = endPortal

        val gap = config.gap.map { it.block.defaultBlockState() }
        val createCeiling = false //  TODO region.dimensionType().hasCeiling()

        val minY = max(chunk.minBuildHeight, config.minY)
        val maxY = min(chunk.maxBuildHeight, config.maxY)

        val mutable = BlockPos.MutableBlockPos()

        val origin = BlockPos(chunk.pos.minBlockX, -minY, chunk.pos.minBlockZ)

        val access = object : BlockAccess(useBarrier = !gap.isPresent) {
            override fun setBlock(state: BlockState, pos: BlockPos) {
                val at = pos.offset(mutable)
                chunk.setBlockState(at, state, false)
            }

            override fun canReplace(pos: BlockPos): Boolean {
                val state = chunk.getBlockState(pos.offset(mutable))
                return gap.map {
                    state.`is`(it.block)
                }.orElseGet {
                    state.isAir
                }
            }

            override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
                with(pos.offset(mutable).offset(origin.x, 0, origin.z)) {
                    nbt.putInt("x", x)
                    nbt.putInt("y", y)
                    nbt.putInt("z", z)
                }
                chunk.setBlockEntityNbt(nbt)
            }
        }

        val hasEndPortal = endPortalPositions.contains(chunk.pos)

        var generatedPortal = false
        for (x in 0 until 16) for (z in 0 until 16) for (y in minY..(maxY + 2)) {

            mutable.set(x, y, z)
            val isFloor = y == minY

            if (config.distance.isBlock(mutable.offset(origin))) {
                if (isFloor && endPortal != null && hasEndPortal && (x > 3 && z > 3) && !generatedPortal) {
                    endPortal.generate(random, access)
                    generatedPortal = true
                } else if (isFloor || (createCeiling && (y - config.distance.y) > maxY)) {
                    access.set(BEDROCK)
                } else config.generate(random, access)
            } else gap.ifPresent {
                access.set(it)
            }

        }

        return CompletableFuture.completedFuture(chunk)
    }

    override fun findNearestMapFeature(
        level: ServerLevel,
        structure: StructureFeature<*>,
        pos: BlockPos,
        something: Int,
        somethingElse: Boolean,
    ): BlockPos? {
        val distance = config.distance
        return if (structure == StructureFeature.STRONGHOLD) {
            endPortalPositions
                .map { BlockPos(it.minBlockX + distance.x, level.minBuildHeight, it.minBlockZ + distance.z) }
                .minByOrNull { it.distSqr(pos) }
        } else super.findNearestMapFeature(level, structure, pos, something, somethingElse)
    }

    override fun climateSampler(): Climate.Sampler {
        return CLIMATE
    }

    override fun buildSurface(region: WorldGenRegion, structures: StructureFeatureManager, chunk: ChunkAccess) {
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
    ): Int {
        return min(config.maxY, accessor.maxBuildHeight)
    }

    override fun getBaseColumn(x: Int, z: Int, accessor: LevelHeightAccessor): NoiseColumn {
        return NoiseColumn(minY, emptyArray<BlockState>())
    }
}