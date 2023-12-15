package com.possible_triangle.skygrid.api.xml.elements

import com.google.common.base.Predicate
import com.possible_triangle.skygrid.api.events.BlockNbtModifier
import com.possible_triangle.skygrid.api.events.RegisterModifiersEvent
import com.possible_triangle.skygrid.api.world.Generator
import com.possible_triangle.skygrid.api.world.IBlockAccess
import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.providers.SingleBlock
import com.possible_triangle.skygrid.platform.Services
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.util.*


@Serializable
@SerialName("dimension")
data class GridConfig(
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
        private set

    @Transient
    private lateinit var modifier: BlockNbtModifier<Boolean>

    override fun generate(random: RandomSource, access: IBlockAccess): Boolean {
        return this.blocks.random(random).generate(
            random,
        ) { state, pos ->
            if (access.set(state, pos)) {
                val block = state.block

                if (block is EntityBlock) {
                    CompoundTag().takeIf { modifier.modify(random, state, pos, it) }?.also { nbt ->
                        block.newBlockEntity(pos, state)?.also {
                            nbt.putString("id", BlockEntityType.getKey(it.type).toString())
                            access.setNBT(pos, nbt)
                        }
                    }
                }

                true
            } else {
                false
            }
        }
    }

    private fun buildModifier(): BlockNbtModifier<Boolean> {
        val modifiers = arrayListOf<BlockNbtModifier.Entry>()

        RegisterModifiersEvent.EVENT.invoke(object : RegisterModifiersEvent {
            override val config = this@GridConfig

            override fun register(predicate: Predicate<BlockState>, modifier: BlockNbtModifier<Unit>) {
                modifiers.add(BlockNbtModifier.Entry(predicate::test, modifier))
            }
        })

        return if (modifiers.isEmpty()) BlockNbtModifier.empty(false)
        else Services.CONFIGS.server.modifierStrategy.filterAndMerge(modifiers)
    }

    fun validate(registries: HolderLookup.Provider, references: IReferenceContext): Boolean {
        val blockRegistry = registries.lookupOrThrow(Registries.BLOCK)
        val entityRegistry = registries.lookupOrThrow(Registries.ENTITY_TYPE)

        loot.validate { true }
        mobs.validate { entityRegistry.get(ResourceKey.create(Registries.ENTITY_TYPE, it.key)).isPresent }
        gap = Optional.ofNullable(unsafeGap).filter { it.validate(blockRegistry, references) }

        modifier = buildModifier()

        return blocks.validate { it.validate(blockRegistry, references) }
    }

}