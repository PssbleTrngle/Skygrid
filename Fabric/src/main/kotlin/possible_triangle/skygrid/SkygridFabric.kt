package possible_triangle.skygrid

import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.server.packs.PackType
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.command.SkygridCommand
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.mixin.WorldPresetMixin
import possible_triangle.skygrid.platform.FabricPlatformHelper
import possible_triangle.skygrid.world.SkygridGenerator

@ExperimentalSerializationApi
@ExperimentalXmlUtilApi
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

        ItemTooltipCallback.EVENT.register(SkygridMod::onItemTooltip)
        CommandRegistrationCallback.EVENT.register { it, _ -> SkygridCommand.register(it) }

        WorldPresetMixin.presets().add(SkygridGenerator)

    }
}