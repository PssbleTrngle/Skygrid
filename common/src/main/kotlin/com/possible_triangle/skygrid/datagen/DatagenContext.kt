package com.possible_triangle.skygrid.datagen

import com.mojang.serialization.JsonOps
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.RegistryOps

data class DatagenContext(val registries: RegistryAccess, val defaultMod: String = "minecraft") {

    val jsonOps = RegistryOps.create(JsonOps.INSTANCE, registries)

}
