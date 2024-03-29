package com.possible_triangle.skygrid.fabric

import com.possible_triangle.skygrid.SkygridMod
import com.possible_triangle.skygrid.api.SkygridConstants.MOD_ID
import com.possible_triangle.skygrid.command.SkygridCommand
import com.possible_triangle.skygrid.fabric.platform.FabricPlatformHelper
import com.possible_triangle.skygrid.world.SkygridPreset
import com.possible_triangle.skygrid.xml.XMLResource
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.core.Registry
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
@Suppress("unused")
object SkygridFabric : ModInitializer {

    override fun onInitialize() {
        SkygridMod.init()
        SkygridMod.setup()
        FabricPlatformHelper.register()

        XMLResource.register {
            val listener = FabricReloadListener(it, it.path)
            ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(listener)
        }

        ServerLifecycleEvents.SERVER_STARTING.register { XMLResource.reload(it) }
        ServerLifecycleEvents.SERVER_STOPPING.register { XMLResource.clear() }

        Registry.register(BuiltinRegistries.WORLD_PRESET, ResourceLocation(MOD_ID, MOD_ID), SkygridPreset())

        CommandRegistrationCallback.EVENT.register { it, ctx, _ -> SkygridCommand.register(it, ctx) }
    }

}