package possible_triangle.skygrid.world

import com.mojang.datafixers.util.Function5
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.tags.ConfiguredStructureTags
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
import net.minecraft.world.level.levelgen.WorldGenSettings
import net.minecraft.world.level.levelgen.blending.Blender
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement
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
    structures: Registry<StructureSet>,
    private val configKey: String,
    private val seed: Long?,
    private val endPortals: Boolean,
) : ChunkGenerator(structures, Optional.empty(), biomeSource) {

    companion object {
        fun create(
            registries: RegistryAccess,
            seed: Long,
            dimension: ResourceKey<LevelStem>?,
            biomeSource: BiomeSource? = null,
        ): ChunkGenerator {
            val biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY)
            val structures = registries.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY)
            val config = dimension?.location() ?: ResourceLocation(SkygridMod.MOD_ID, "default")
            return SkygridChunkGenerator(
                biomeSource ?: FixedBiomeSource(biomes.getHolderOrThrow(Biomes.THE_VOID)),
                structures,
                config.toString(),
                seed,
                dimension == LevelStem.OVERWORLD)
        }

        fun createSettings(
            registries: RegistryAccess,
            seed: Long,
            bonusChest: Boolean,
        ): WorldGenSettings {
            val dimensions = MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null)
            val biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY)

            fun register(
                stem: ResourceKey<LevelStem>,
                dimension: ResourceKey<DimensionType>,
                biomeSource: BiomeSource,
            ) {
                val generator = create(registries, seed, stem, biomeSource)
                dimensions.register(stem, LevelStem(
                    registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getHolderOrThrow(dimension),
                    generator), Lifecycle.stable())
            }

            register(
                LevelStem.OVERWORLD,
                DimensionType.OVERWORLD_LOCATION,
                FixedBiomeSource(biomes.getHolderOrThrow(Biomes.PLAINS))
            )

            register(LevelStem.NETHER,
                DimensionType.NETHER_LOCATION,
                FixedBiomeSource(biomes.getHolderOrThrow(Biomes.NETHER_WASTES))
            )

            register(LevelStem.END,
                DimensionType.END_LOCATION,
                FixedBiomeSource(biomes.getHolderOrThrow(Biomes.THE_END))
            )

            return WorldGenSettings(seed, false, bonusChest, dimensions)
        }


        private val BEDROCK = Blocks.BEDROCK.defaultBlockState()

        val CODEC: Codec<SkygridChunkGenerator> = RecordCodecBuilder.create { builder ->
            commonCodec(builder).and(builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                Codec.STRING.fieldOf("config").forGetter { it.configKey },
                Codec.LONG.optionalFieldOf("seed").forGetter { Optional.ofNullable(it.seed) },
                Codec.BOOL.optionalFieldOf("endPortals").forGetter { Optional.of(it.endPortals) },
            )).apply(
                builder,
                builder.stable(Function5 { structures, source, key, seed, endPortals ->
                    SkygridChunkGenerator(
                        source,
                        structures,
                        key,
                        seed.orElse(null),
                        endPortals.orElse(false),
                    )
                }),
            )
        }
    }

    private val climate = Climate.empty()

    override fun codec(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun withSeed(seed: Long): SkygridChunkGenerator {
        return SkygridChunkGenerator(biomeSource, structureSets, configKey, seed, endPortals)
    }

    private val random = seed?.let { Random(it) } ?: Random
    private val strongholdRandom = Random(random.nextLong())

    private val config
        get() = DimensionConfig[ResourceLocation(configKey)] ?: DimensionConfig.DEFAULT

    private val endPortal
        get() = Preset[ResourceLocation("end_portal")]

    private val strongholdSettings =
        if (endPortals) ConcentricRingsStructurePlacement(32, 3, 128)
        else null

    private val endPortalPositions: List<ChunkPos> = strongholdSettings?.let {
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
        structures: HolderSet<ConfiguredStructureFeature<*, *>?>,
        pos: BlockPos,
        something: Int,
        somethingElse: Boolean,
    ): Pair<BlockPos, Holder<ConfiguredStructureFeature<*, *>>>? {
        val distance = config.distance
        return if (structures == ConfiguredStructureTags.EYE_OF_ENDER_LOCATED) {
            val pos = endPortalPositions.map {
                BlockPos(it.minBlockX + distance.x,
                    level.minBuildHeight,
                    it.minBlockZ + distance.z)
            }.minByOrNull { it.distSqr(pos) }
            Pair(pos, null)
        } else super.findNearestMapFeature(level, structures, pos, something, somethingElse)
    }

    override fun climateSampler(): Climate.Sampler {
        return climate
    }

    override fun buildSurface(region: WorldGenRegion, structures: StructureFeatureManager, chunk: ChunkAccess) {
        // No surface
    }

    override fun applyCarvers(
        region: WorldGenRegion,
        seed: Long,
        biomes: BiomeManager,
        structures: StructureFeatureManager,
        chunk: ChunkAccess,
        step: GenerationStep.Carving,
    ) {
        // No carving
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
    ): Int {
        return min(config.maxY, accessor.maxBuildHeight)
    }

    override fun getBaseColumn(x: Int, z: Int, accessor: LevelHeightAccessor): NoiseColumn {
        return NoiseColumn(minY, emptyArray<BlockState>())
    }

    override fun addDebugScreenInfo(info: MutableList<String>, pos: BlockPos) {
        // No debug screen info for now
    }
}