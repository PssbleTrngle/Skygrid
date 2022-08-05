package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagKey
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.xml.impl.LootTable
import possible_triangle.skygrid.data.xml.impl.SingleBlock
import possible_triangle.skygrid.data.xml.impl.SpawnerEntry
import possible_triangle.skygrid.world.BlockAccess
import possible_triangle.skygrid.world.Generator
import java.util.*
import kotlin.random.Random

fun Collection<BlockProvider>.flat(): List<Pair<Block, Double>> {
    val totalWeight = sumOf { it.weight }
    return flatMap { provider ->
        val probability = provider.weight / totalWeight
        val extras = provider.validExtras.flatMap { extra ->
            extra.validProviders.flat().map { it.first to it.second * extra.probability * probability }
        }
        extras + provider.flat().map { it.first to it.second * probability }
    }
}

fun Collection<BlockProvider>.weights(): Map<Block, Double> {
    val flat = this.flat()
    return hashMapOf<Block, Double>().apply {
        flat.forEach { (block, weight) ->
            put(block, getOrDefault(block, 0.0) + weight)
        }
    }
}

@Serializable
@SerialName("dimension")
data class DimensionConfig(
    @XmlSerialName("blocks", "", "") val blocks: ListWrapper<BlockProvider>,
    @XmlSerialName("loot", "", "") val loot: ListWrapper<LootTable> = ListWrapper(),
    @XmlSerialName("mobs", "", "") val mobs: ListWrapper<SpawnerEntry> = ListWrapper(),
    val replace: Boolean = false,
    val minY: Int = Int.MIN_VALUE,
    val maxY: Int = 100,
    val distance: Distance = Distance(4, 4, 4),
    @XmlSerialName("gap", "", "") private val unsafeGap: SingleBlock? = null,
) : Generator<BlockAccess> {

    @Transient
    lateinit var gap: Optional<SingleBlock>


    fun validate(registries: RegistryAccess): Boolean {
        val blockRegistry = registries.registryOrThrow(Registry.BLOCK_REGISTRY)
        val entityRegistry = registries.registryOrThrow(Registry.ENTITY_TYPE_REGISTRY)
        val references = ReferenceContext()

        val getTag = { it: TagKey<Block> -> blockRegistry.getTagOrEmpty(it).map { it.value() } }

        loot.validate { true }
        mobs.validate { entityRegistry.containsKey(it.key) }
        gap = Optional.ofNullable(unsafeGap).filter { it.validate(blockRegistry, references) }
        return blocks.validate { it.validate(blockRegistry, references) }
    }

    override fun generate(random: Random, access: BlockAccess): Boolean {
        val generateLoot = loot.isValid()
        val fillSpawners = mobs.isValid()

        return this.blocks.random(random).generate(
            random,
        ) { state, pos ->
            if (access.set(state, pos)) {
                val block = state.block

                if (block is EntityBlock) {
                    val nbt = if (generateLoot && state.`is`(SkygridMod.LOOT_CONTAINERS)) {
                        CompoundTag().apply {
                            putString("id", "mob_spawner")
                            val lootTable = loot.random(random)
                            putString("LootTable", lootTable.id)
                            putLong("LootTableSeed", random.nextLong())
                        }
                    } else if (fillSpawners && state.`is`(Blocks.SPAWNER)) {
                        CompoundTag().apply {
                            val mob = mobs.random(random)
                            val data = mob.createSpawnData()
                            val potentials = SimpleWeightedRandomList.builder<SpawnData>().add(data, 1).build()

                            SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, data).result().ifPresent {
                                put("SpawnData", it)
                            }

                            SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, potentials).result().ifPresent {
                                put("SpawnPotentials", it)
                            }
                        }
                    } else null

                    if (nbt != null) block.newBlockEntity(pos, state)?.also {
                        nbt.putString("id", BlockEntityType.getKey(it.type).toString())
                        access.setNBT(pos, nbt)
                    }
                }

                true
            } else {
                false
            }
        }
    }

    companion object : XMLResource<DimensionConfig>("dimensions", { DimensionConfig.serializer() }) {

        val DEFAULT = DimensionConfig(ListWrapper(SingleBlock("bedrock")))

        private val WEIGHT_MAP = hashMapOf<Block, HashMap<ResourceLocation, Double>>()

        fun getProbability(block: Block): Map<ResourceLocation, Double> {
            return WEIGHT_MAP[block] ?: emptyMap()
        }

        override fun merge(a: DimensionConfig, b: DimensionConfig): DimensionConfig {
            return if (b.replace) b
            else b.copy(
                replace = false,
                blocks = a.blocks + b.blocks,
                loot = a.loot + b.loot,
                mobs = a.mobs + b.mobs,
            )
        }

        override fun onReload(server: MinecraftServer) {
            validate(DEFAULT, server)

            entries.forEach { (key, config) ->
                val blocks = config.blocks.flat()
                blocks.forEach { (block, probability) ->
                    WEIGHT_MAP.getOrPut(block) { hashMapOf() }[key] = probability
                }
            }
        }

        override fun validate(value: DimensionConfig, server: MinecraftServer): Boolean {
            return value.validate(server.registryAccess())
        }

    }

}