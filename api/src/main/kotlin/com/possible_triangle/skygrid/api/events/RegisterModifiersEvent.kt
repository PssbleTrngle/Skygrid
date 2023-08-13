package com.possible_triangle.skygrid.api.events

import com.google.common.base.Predicate
import com.possible_triangle.skygrid.api.xml.elements.DimensionConfig
import com.possible_triangle.skygrid.platform.Services
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState

interface RegisterModifiersEvent {

    companion object {
        val EVENT = Services.EVENTS.createEvent(RegisterModifiersEvent::class)
    }

    val config: DimensionConfig

    fun register(predicate: Predicate<BlockState>, modifier: BlockNbtModifier<Unit>)

}

fun interface BlockNbtModifier<T> {

    fun modify(random: RandomSource, state: BlockState, pos: BlockPos, nbt: CompoundTag): T

    data class Entry(val filter: (BlockState) -> Boolean, val modifier: BlockNbtModifier<Unit>)

    companion object {
        fun <T> empty(value: T) = BlockNbtModifier { _, _, _, _ -> value }
    }

}