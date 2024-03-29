package com.possible_triangle.skygrid.fabric

import com.possible_triangle.skygrid.api.SkygridConstants
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class FabricReloadListener(private val internal: PreparableReloadListener, private val id: String) :
    IdentifiableResourceReloadListener {

    override fun reload(
        preparationBarrier: PreparableReloadListener.PreparationBarrier,
        resourceManager: ResourceManager,
        profilerFiller: ProfilerFiller,
        profilerFiller2: ProfilerFiller,
        executor: Executor,
        executor2: Executor,
    ): CompletableFuture<Void> {
        return internal.reload(preparationBarrier,
            resourceManager,
            profilerFiller,
            profilerFiller2,
            executor,
            executor2)
    }

    override fun getFabricId(): ResourceLocation {
        return ResourceLocation(SkygridConstants.MOD_ID, id)
    }

}