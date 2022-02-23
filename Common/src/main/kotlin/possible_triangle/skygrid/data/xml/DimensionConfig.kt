package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagContainer
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.Constants
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.xml.impl.LootTable
import possible_triangle.skygrid.data.xml.impl.SpawnerEntry
import possible_triangle.skygrid.world.BlockAccess
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
    val minY: Int = Int.MIN_VALUE,
    val maxY: Int = 100,
    val distance: Distance = Distance(4, 4, 4),
    private @XmlSerialName("gap", "", "") val unsafeGap: SingleBlock? = null,
) {

    @Transient
    lateinit var gap: Optional<SingleBlock>

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        this.loot.validate { true }
        this.mobs.validate { true }
        gap = Optional.ofNullable(unsafeGap).filter { it.validate(blocks, tags) }
        return this.blocks.validate { it.validate(blocks, tags) }
    }

    fun generate(random: Random, access: BlockAccess) {
        val generateLoot = loot.children.isNotEmpty()
        val fillSpawners = mobs.children.isNotEmpty()

        this.blocks.random(random).generate(random) { state, pos ->
            access.set(state, pos)
            val nbt = if (generateLoot && state.`is`(Constants.LOOT_CONTAINERS)) {
                CompoundTag().apply {
                    val lootTable = loot.random(random)
                    putString("LootTable", lootTable.toString())
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
            } else {
                null
            }

            if (nbt != null) access.setNBT(pos, nbt)
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
            )
        }

        override fun onReload(server: MinecraftServer) {
            validate(DEFAULT, server)
        }

        override fun validate(value: DimensionConfig, server: MinecraftServer): Boolean {
            return value.validate(server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY), server.tags)
        }

    }

}