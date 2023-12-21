package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.datagen.GridConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EndPortalFrameBlock
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path

@ExperimentalSerializationApi
@ExperimentalXmlUtilApi
class Presets(output: Path) : GridConfigGenerator("presets", output) {

    override fun generate() {

        val rangeNP1 = -1..1

        preset("end_portal") {
            block(Blocks.AIR) {
                for (x in rangeNP1) for (z in rangeNP1) if (x != 0 || z != 0) {
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

        preset("spawner") {
            block(Blocks.SPAWNER) {
                for (x in rangeNP1) for (y in rangeNP1) for (z in rangeNP1)
                    if (intArrayOf(x, y, z).count { it == 0 } == 1) {
                        offset(x = x, y = y, z = z) {
                            block(Blocks.COBBLESTONE)
                            block(Blocks.MOSSY_COBBLESTONE)
                        }
                    }
            }
        }

        preset("redstone_lamp") {
            block(Blocks.REDSTONE_LAMP) {
                side(Direction.UP, probability = 0.2) {
                    block(Blocks.REDSTONE_WIRE)
                    tag(BlockTags.PRESSURE_PLATES) {
                        except {
                            pattern("polished_blackstone_pressure_plate")
                            pattern("crimson_pressure_plate")
                            pattern("warped_pressure_plate")
                        }
                    }
                }
                cardinal3D(probability = 0.2) {
                    block(Blocks.LEVER)
                    tag(BlockTags.BUTTONS) {
                        except {
                            pattern("polished_blackstone_button")
                            pattern("crimson_button")
                            pattern("warped_button")
                        }
                    }
                }
            }
        }

    }

}