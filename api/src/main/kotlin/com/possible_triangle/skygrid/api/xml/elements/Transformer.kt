package com.possible_triangle.skygrid.api.xml.elements

import kotlinx.serialization.Serializable
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState

@Serializable
abstract class Transformer {

    abstract fun apply(state: BlockState, random: RandomSource): BlockState

}