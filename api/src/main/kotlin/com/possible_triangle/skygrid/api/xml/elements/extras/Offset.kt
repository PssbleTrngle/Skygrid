package com.possible_triangle.skygrid.api.xml.elements.extras

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource

@Serializable
@SerialName("offset")
data class Offset(
    val x: Int  = 0,
    val y: Int  = 0,
    val z: Int  = 0,
    override val providers: List<BlockProvider>,
    override val probability: Double = 1.0,
    override val shared: Boolean = false,
) : Extra() {

    override fun internalValidate(blocks: HolderLookup.RegistryLookup<Block>): Boolean {
        return true
    }

    override fun offset(pos: BlockPos, random: RandomSource): BlockPos {
        return pos.offset(x, y, z)
    }

}