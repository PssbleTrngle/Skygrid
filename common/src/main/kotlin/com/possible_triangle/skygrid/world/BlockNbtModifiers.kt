package com.possible_triangle.skygrid.world

import com.possible_triangle.skygrid.api.SkygridTags
import com.possible_triangle.skygrid.api.events.BlockNbtModifier
import com.possible_triangle.skygrid.api.events.RegisterModifiersEvent
import com.possible_triangle.skygrid.platform.services.IConfigs
import net.minecraft.nbt.NbtOps
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.block.Blocks

object BlockNbtModifiers {

    fun RegisterModifiersEvent.registerDefaultModifiers() {
        val generateLoot = config.loot.isValid()
        val fillSpawners = config.mobs.isValid()

        if (generateLoot) register({ it.`is`(SkygridTags.LOOT_CONTAINERS) }) { random, _, _, nbt ->
            nbt.putString("id", "mob_spawner")
            val lootTable = config.loot.random(random)
            nbt.putString("LootTable", lootTable.id)
            nbt.putLong("LootTableSeed", random.nextLong())
        }

        if (fillSpawners) register({ it.`is`(Blocks.SPAWNER) }) { random, _, _, nbt ->
            val mob = config.mobs.random(random)
            val data = mob.createSpawnData()
            val potentials = SimpleWeightedRandomList.builder<SpawnData>().add(data, 1).build()

            SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, data).result().ifPresent {
                nbt.put("SpawnData", it)
            }

            SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, potentials).result().ifPresent {
                nbt.put("SpawnPotentials", it)
            }
        }

    }

    @Suppress("unused")
    enum class ModifierStrategy(private val logic: IConfigs.IModifierStrategy) : IConfigs.IModifierStrategy {
        FIRST_ONLY({ modifiers ->
            BlockNbtModifier { random, state, pos, nbt ->
                modifiers
                    .filter { it.filter(state) }
                    .map { it.modifier.modify(random, state, pos, nbt) }
                    .isNotEmpty()
            }
        }),

        ALL_MATCHING({ modifiers ->
            BlockNbtModifier { random, state, pos, nbt ->
                modifiers
                    .firstOrNull { it.filter(state) }
                    ?.apply { modifier.modify(random, state, pos, nbt) }
                    .let { it != null }
            }
        });

        override fun filterAndMerge(modifiers: Collection<BlockNbtModifier.Entry>) = logic.filterAndMerge(modifiers)
    }

}
