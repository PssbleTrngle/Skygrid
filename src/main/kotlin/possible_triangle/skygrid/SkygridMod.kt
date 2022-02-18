package possible_triangle.skygrid

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import possible_triangle.skygrid.config.impl.*
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.world.SkygridGenerator
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerObject
import java.io.File

@ExperimentalSerializationApi
@Mod(SkygridMod.ID)
object SkygridMod {
    const val ID = "skygrid"

    val LOGGER: Logger = LogManager.getLogger(ID)

    private val WORLD_TYPES = DeferredRegister.create(ForgeRegistries.WORLD_TYPES, ID)

    val EXAMPLE = DimensionConfig(BlockList(listOf(
        Block("dirt", weight = 0.5),
        Block("grass_block", weight = 0.5, sides = listOf(Side(on = "up", listOf(Block("grass"))))),
        BlockList(
            weight = 0.9, name = "test",
            children = listOf(
                Tag("logs", filters = listOf(ExceptFilter(listOf(NameFilter("_wood"))))),
                Block(
                    "red_glass", weight = 0.5,
                    sides = listOf(Side(on = "north", listOf(Block("green_glass"))))),
            ), sides = listOf(Side(on = "up", listOf(Block("end_rod"))))
        ))
    ))

    init {
        LOGGER.info("Skygrid booting")

        WORLD_TYPES.registerObject("skygrid") { SkygridGenerator() }
        WORLD_TYPES.register(MOD_BUS)

        File("example.xml").writeText(XMLResource.LOADER.encodeToString(EXAMPLE))

        XMLResource.LOADER.decodeFromString<DimensionConfig>(XMLResource.LOADER.encodeToString(EXAMPLE))

        XMLResource.LOADER.decodeFromString<DimensionConfig>(File("C:\\Users\\nik\\Desktop\\Repos\\Minecraft\\Skygrid\\src\\main\\resources\\data\\minecraft\\skygrid\\dimensions\\overworld.xml").readLines()
            .joinToString("\n"))

    }

}