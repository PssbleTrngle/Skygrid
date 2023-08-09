package com.possible_triangle.skygrid.api.xml.elements

import com.possible_triangle.skygrid.api.SkygridTags
import com.possible_triangle.skygrid.api.world.Generator
import com.possible_triangle.skygrid.api.world.IBlockAccess
import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.providers.SingleBlock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.util.*


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
) : Generator<IBlockAccess> {

    @Transient
    lateinit var gap: Optional<SingleBlock>

    override fun generate(random: RandomSource, access: IBlockAccess): Boolean {
        val generateLoot = loot.isValid()
        val fillSpawners = mobs.isValid()

        return this.blocks.random(random).generate(
            random,
        ) { state, pos ->
            if (access.set(state, pos)) {
                val block = state.block

                if (block is EntityBlock) {
                    val nbt = if (generateLoot && state.`is`(SkygridTags.LOOT_CONTAINERS)) {
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

    fun validate(registries: RegistryAccess, references: IReferenceContext): Boolean {
        val blockRegistry = registries.registryOrThrow(Registry.BLOCK_REGISTRY)
        val entityRegistry = registries.registryOrThrow(Registry.ENTITY_TYPE_REGISTRY)

        val getTag = { it: TagKey<Block> -> blockRegistry.getTagOrEmpty(it).map { it.value() } }

        loot.validate { true }
        mobs.validate { entityRegistry.containsKey(it.key) }
        gap = Optional.ofNullable(unsafeGap).filter { it.validate(blockRegistry, references) }
        return blocks.validate { it.validate(blockRegistry, references) }
    }

}