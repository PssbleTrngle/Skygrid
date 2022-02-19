package possible_triangle.skygrid

import kotlinx.serialization.Serializable
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.DimensionConfig
import possible_triangle.skygrid.config.Preset
import possible_triangle.skygrid.world.SkygridGenerator
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerObject

@Mod(SkygridMod.ID)
object SkygridMod {
    const val ID = "skygrid"

    val LOGGER: Logger = LogManager.getLogger(ID)

    private val WORLD_TYPES = DeferredRegister.create(ForgeRegistries.WORLD_TYPES, ID)

    init {
        LOGGER.info("Skygrid booting")

        WORLD_TYPES.registerObject("skygrid") { SkygridGenerator() }
        WORLD_TYPES.register(MOD_BUS)

        Preset.register()
        DimensionConfig.register()

    }

}