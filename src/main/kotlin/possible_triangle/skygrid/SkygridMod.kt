package possible_triangle.skygrid

import net.minecraft.core.Registry
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import possible_triangle.skygrid.block.StiffAir
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Preset
import possible_triangle.skygrid.world.SkygridChunkGenerator
import possible_triangle.skygrid.world.SkygridGenerator
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerObject

@Mod(SkygridMod.ID)
object SkygridMod {
    const val ID = "skygrid"

    val LOGGER: Logger = LogManager.getLogger(ID)

    private val WORLD_TYPES = DeferredRegister.create(ForgeRegistries.WORLD_TYPES, ID)
    private val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID)

    val STIFF_AIR by BLOCKS.registerObject("stiff_air") { StiffAir() }

    init {
        LOGGER.info("Skygrid booting")

        WORLD_TYPES.registerObject(ID) { SkygridGenerator() }

        WORLD_TYPES.register(MOD_BUS)
        BLOCKS.register(MOD_BUS)

        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(ID, ID), SkygridChunkGenerator.CODEC)

        Preset.register()
        DimensionConfig.register()

        FORGE_BUS.addListener { event: ItemTooltipEvent ->
            val item = event.itemStack.item
            if (item is BlockItem && event.flags.isAdvanced) {
                item.block.tags.forEach {
                    event.toolTip.add(TextComponent("#$it"))
                }
            }
        }

    }

}