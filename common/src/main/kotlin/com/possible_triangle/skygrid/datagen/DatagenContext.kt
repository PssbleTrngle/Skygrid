package com.possible_triangle.skygrid.datagen

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import net.minecraft.core.HolderLookup
import net.minecraft.resources.RegistryOps
import java.util.concurrent.CompletableFuture

data class DatagenContext(val lookup: CompletableFuture<HolderLookup.Provider>, val defaultMod: String = "minecraft") {

    val jsonOps: CompletableFuture<RegistryOps<JsonElement>> = lookup.thenApply {
        RegistryOps.create(JsonOps.INSTANCE, it)
    }

}
