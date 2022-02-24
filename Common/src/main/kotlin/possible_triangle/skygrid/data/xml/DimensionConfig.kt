package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagContainer
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.xml.impl.LootTable
import possible_triangle.skygrid.data.xml.impl.SpawnerEntry
import possible_triangle.skygrid.world.BlockAccess
import possible_triangle.skygrid.world.Generator
import java.util.*
import kotlin.random.Random
import possible_triangle.skygrid.data.xml.impl.Block as SingleBlock

@Serializable
@SerialName("dimension")
data class DimensionConfig(
    @XmlSerialName("blocks", "", "") val blocks: ListWrapper<BlockProvider>,
    @XmlSerialName("loot", "", "") val loot: ListWrapper<LootTable> = ListWrapper(),
    @XmlSerialName("mobs", "", "") val mobs: ListWrapper<SpawnerEntry> = ListWrapper(),
    val replace: Boolean = false,
    val endPortals: Boolean = false,
    val minY: Int = Int.MIN_VALUE,
    val maxY: Int = 100,
    val distance: Distance = Distance(4, 4, 4),
    @XmlSerialName("gap", "", "") private val unsafeGap: SingleBlock? = null,
) : Generator<BlockAccess> {

    @Transient
    lateinit var gap: Optional<SingleBlock>

    fun validate(registries: RegistryAccess, tags: TagContainer): Boolean {
        val blockRegistry = registries.registryOrThrow(Registry.BLOCK_REGISTRY)
        val entityRegistry = registries.registryOrThrow(Registry.ENTITY_TYPE_REGISTRY)
        loot.validate { true }
        mobs.validate { entityRegistry.containsKey(it.key) }
        gap = Optional.ofNullable(unsafeGap).filter { it.validate(blockRegistry, tags) }
        return blocks.validate { it.validate(blockRegistry, tags) }
    }

    override fun generate(random: Random, access: BlockAccess): Boolean {
        val generateLoot = loot.isValid()
        val fillSpawners = mobs.isValid()

        return this.blocks.random(random).generate(random) { state, pos ->
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
        }

        override fun validate(value: DimensionConfig, server: MinecraftServer): Boolean {
            return value.validate(server.registryAccess(), server.tags)
        }

    }

}