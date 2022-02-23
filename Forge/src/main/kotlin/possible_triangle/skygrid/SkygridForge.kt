package possible_triangle.skygrid

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import net.minecraft.world.level.block.Block
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.server.ServerAboutToStartEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.Constants.MOD_ID
import possible_triangle.skygrid.block.StiffAir
import possible_triangle.skygrid.command.SkygridCommand
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.world.SkygridGenerator
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerObject

@ExperimentalSerializationApi
@ExperimentalXmlUtilApi
@Mod(MOD_ID)
object SkygridForge {

    private val WORLD_TYPES = DeferredRegister.create(ForgeRegistries.WORLD_TYPES, MOD_ID)
    private val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID)

    private val STIFF_AIR by BLOCKS.registerObject("stiff_air") { StiffAir() }

    fun getStiffAir(): Block {
        return STIFF_AIR
    }

    init {
        CommonClass.init()

        FORGE_BUS.addListener(EventPriority.HIGH) { event: AddReloadListenerEvent -> XMLResource.register(event::addListener) }
        FORGE_BUS.addListener { _: ServerStoppingEvent -> XMLResource.clear() }
        FORGE_BUS.addListener { event: ServerAboutToStartEvent -> XMLResource.reload(event.server) }

        WORLD_TYPES.registerObject(MOD_ID) { SkygridGenerator() }

        WORLD_TYPES.register(MOD_BUS)
        BLOCKS.register(MOD_BUS)

        FORGE_BUS.addListener { event: ItemTooltipEvent ->
            CommonClass.onItemTooltip(event.itemStack, event.flags, event.toolTip)
        }

        FORGE_BUS.addListener { event: RegisterCommandsEvent ->
            SkygridCommand.register(event.dispatcher)
        }

    }

}