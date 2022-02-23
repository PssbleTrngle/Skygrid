package possible_triangle.skygrid

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import possible_triangle.skygrid.platform.Services

object Constants {
    const val MOD_ID = "skygrid"
    const val MOD_NAME = "Skygrid"
    val LOGGER = LogManager.getLogger(MOD_NAME)

    val AMETHYST_CLUSTERS = Services.PLATFORM.createBlockTag(ResourceLocation(MOD_ID, "amethyst_clusters"))
    val LOOT_CONTAINERS = Services.PLATFORM.createBlockTag(ResourceLocation(MOD_ID, "loot_containers"))
}