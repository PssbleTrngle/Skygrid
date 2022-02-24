package possible_triangle.skygrid.world

import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.WorldGenRegion
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
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.xml.DimensionConfig
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class SkygridChunkGenerator(
    biomeSource: BiomeSource,
    private val configKey: String,
    private val seed: Long?,
) : ChunkGenerator(biomeSource, StructureSettings(false)) {

    companion object {
        fun create(
            registries: RegistryAccess,
            seed: Long,
            dimension: ResourceKey<LevelStem>?,
        ): ChunkGenerator {
            val biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY)
            val config = dimension?.location() ?: ResourceLocation(SkygridMod.MOD_ID, "default")
            return SkygridChunkGenerator(
                FixedBiomeSource(biomes.getOrThrow(Biomes.THE_VOID)), config.toString(), seed,
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
                registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrThrow(DimensionType.OVERWORLD_LOCATION)
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
                Codec.LONG.optionalFieldOf("seed").forGetter { Optional.ofNullable(it.seed) }
            ).apply(
                builder,
                builder.stable(Function3 { source, key, seed ->
                    SkygridChunkGenerator(source, key, seed.orElse(null))
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

    private val random = seed?.let { Random(it) } ?: Random

    override fun buildSurface(region: WorldGenRegion, structures: StructureFeatureManager, chunk: ChunkAccess) {
        val config = DimensionConfig[ResourceLocation(configKey)] ?: DimensionConfig.DEFAULT
        val gap = config.gap.map { it.block.defaultBlockState() }

        val minY = max(chunk.minBuildHeight, config.minY)
        val maxY = min(chunk.maxBuildHeight, config.maxY)

        val origin = BlockPos.MutableBlockPos()

        val access = object : BlockAccess(useBarrier = !gap.isPresent) {
            override fun setBlock(state: BlockState, pos: BlockPos) {
                val at = pos.offset(origin)
                chunk.setBlockState(at, state, false)
            }

            override fun canReplace(pos: BlockPos): Boolean {
                val state = chunk.getBlockState(pos.offset(origin))
                return gap.map {
                    state.`is`(it.block)
                }.orElseGet {
                    state.isAir
                }
            }

            override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
                with(pos.offset(origin)) {
                    nbt.putInt("x", x)
                    nbt.putInt("y", y)
                    nbt.putInt("z", z)
                }
                chunk.setBlockEntityNbt(nbt)
            }
        }

        val createCeiling = region.dimensionType().hasCeiling()

        for (x in 0 until 16)
            for (z in 0 until 16)
                for (y in minY..(maxY + 2)) {
                    origin.set(x, y, z)
                    if (config.distance.isBlock(origin.offset(chunk.pos.minBlockX, -minY, chunk.pos.minBlockZ))) {
                        if ((y == minY) || (createCeiling && (y - config.distance.y) > maxY)) {
                            access.set(BEDROCK)
                        } else config.generate(random, access)
                    } else gap.ifPresent {
                        access.set(it)
                    }
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