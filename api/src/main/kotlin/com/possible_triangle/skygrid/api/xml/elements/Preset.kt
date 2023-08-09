package com.possible_triangle.skygrid.api.xml.elements

import com.possible_triangle.skygrid.api.world.Generator
import com.possible_triangle.skygrid.api.world.IBlockAccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.RandomSource

@Serializable
@SerialName("preset")
data class Preset(val provider: BlockProvider) : Generator<IBlockAccess> {

    override fun generate(random: RandomSource, access: IBlockAccess): Boolean {
        return provider.generate(random, access, )
    }

}


