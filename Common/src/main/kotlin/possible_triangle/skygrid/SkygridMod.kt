package possible_triangle.skygrid

import net.minecraft.ChatFormatting
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
    val BARRELS = Services.PLATFORM.createBlockTag(ResourceLocation(MOD_ID, "barrels"))
    val CHESTS = Services.PLATFORM.createBlockTag(ResourceLocation(MOD_ID, "chests"))

    val STIFF_AIR by Services.PLATFORM.createBlock("stiff_air") { StiffAir() }

    fun init() {
        Preset.register()
        DimensionConfig.register()
    }

    fun setup() {
        LOGGER.info("Skygrid booting")
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(MOD_ID, MOD_ID), SkygridChunkGenerator.CODEC)
    }

    fun onItemTooltip(stack: ItemStack, flags: TooltipFlag, tooltip: MutableList<Component>) {
        val item = stack.item
        if (flags.isAdvanced && item is BlockItem) {

            if (Services.CONFIG.showProbabilities) {
                val probabilities = DimensionConfig.getProbability(item.block)
                if (probabilities.isNotEmpty()) {
                    tooltip.add(TextComponent("Probabilities:").withStyle(ChatFormatting.GOLD))
                    probabilities.forEach { (config, probability) ->
                        tooltip.add(TextComponent("  $config: ")
                            .append(TextComponent("${String.format("%.3f", probability * 100)}%").withStyle(
                                ChatFormatting.AQUA))
                        )
                    }
                }
            }

            if (Services.CONFIG.showBlockTags) {
                Services.PLATFORM.getTags(item.block).forEach {
                    tooltip.add(TextComponent("#$it"))
                }
            }

        }
    }

}