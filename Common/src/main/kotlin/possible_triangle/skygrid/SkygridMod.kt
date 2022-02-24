package possible_triangle.skygrid

import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import org.apache.logging.log4j.LogManager
import possible_triangle.skygrid.block.StiffAir
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Preset
import possible_triangle.skygrid.platform.Services
import possible_triangle.skygrid.world.SkygridChunkGenerator

object SkygridMod {

    const val MOD_ID = "skygrid"
    private const val MOD_NAME = "Skygrid"
    val LOGGER = LogManager.getLogger(MOD_NAME)!!

    val AMETHYST_CLUSTERS = Services.PLATFORM.createBlockTag(ResourceLocation(MOD_ID, "amethyst_clusters"))
    val LOOT_CONTAINERS = Services.PLATFORM.createBlockTag(ResourceLocation(MOD_ID, "loot_containers"))

    val STIFF_AIR by Services.PLATFORM.createBlock("stiff_air") { StiffAir() }

    fun init() {
        LOGGER.info("Skygrid booting")

        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(MOD_ID, MOD_ID), SkygridChunkGenerator.CODEC)

        Preset.register()
        DimensionConfig.register()
    }

    fun onItemTooltip(stack: ItemStack, flags: TooltipFlag, tooltip: MutableList<Component>) {
        if (Services.PLATFORM.isDevelopmentEnvironment) {
            val item = stack.item
            if (item is BlockItem && flags.isAdvanced) {
                val tags = Services.PLATFORM.getTags(item.block)
                tags.forEach {
                    tooltip.add(TextComponent("#$it"))
                }
            }
        }
    }
}