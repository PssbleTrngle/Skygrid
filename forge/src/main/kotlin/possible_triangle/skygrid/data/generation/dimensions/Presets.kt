package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EndPortalFrameBlock
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator

@ExperimentalSerializationApi
@ExperimentalXmlUtilApi
class Presets(generator: DataGenerator) : DimensionConfigGenerator("presets", generator) {

    override fun generate() {

        preset("end_portal") {
            block(Blocks.AIR) {
                for (x in -1..1) for (z in -1..1) if (x != 0 || z != 0) {
                    offset(x = x, z = z) {
                        block(Blocks.END_PORTAL_FRAME) {
                            val facing = when (z) {
                                1 -> Direction.EAST
                                -1 -> Direction.WEST
                                else -> when (x) {
                                    -1 -> Direction.SOUTH
                                    else -> Direction.NORTH
                                }
                            }

                            property(EndPortalFrameBlock.FACING, facing)
                        }
                    }
                }
            }
        }

    }

}