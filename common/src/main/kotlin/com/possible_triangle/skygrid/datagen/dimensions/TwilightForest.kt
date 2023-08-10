package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.datagen.CompatMods.TWILIGHT
import com.possible_triangle.skygrid.datagen.DimensionConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction.DOWN
import net.minecraft.core.Direction.UP
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class TwilightForest(generator: DataGenerator) : DimensionConfigGenerator("twilight_forest", generator) {

    private val woods = mapOf(
        "twilight_oak" to 1.0,
        "canopy" to 1.0,
        "dark" to 1.0,
        "time" to 0.1,
        "transformation" to 0.1,
        "mining" to 0.1,
        "sorting" to 0.1,
    )

    override fun generate() {

        dimension(ResourceLocation(TWILIGHT, "twilight_forest")) {
            blocks {
                list("ground") {
                    block(Blocks.GRASS_BLOCK) {
                        side(UP) {
                            block(Blocks.GRASS)
                            block(Blocks.FERN)

                            block("fiddlehead", mod = TWILIGHT)
                            block("mayapple", mod = TWILIGHT)

                            list("saplings") {
                                woods.forEach { (wood, weight) ->
                                    block("${wood}_sapling", mod = TWILIGHT, weight)
                                }
                            }
                        }
                    }

                    block(Blocks.DIRT) {
                        side(DOWN) {
                            block("torchberry_plant", mod = TWILIGHT)
                        }
                    }

                    block(Blocks.STONE)
                }

                list("wood") {
                    list("logs") {
                        cycle(AXIS)
                        woods.forEach { (wood, weight) ->
                            block("${wood}_log", mod = TWILIGHT, weight)
                            block("hollow_${wood}_log_horizontal", mod = TWILIGHT, weight)
                        }
                        side(UP, probability = 0.5) {
                            block("moss_patch", mod = TWILIGHT)
                        }
                    }

                    list("leaves") {
                        woods.forEach { (wood, weight) ->
                            block("${wood}_leaves", mod = TWILIGHT, weight)
                        }
                    }

                    list("roots") {
                        block("root", mod = TWILIGHT)
                        block("liveroot_block", mod = TWILIGHT, weight = 0.1)
                        side(DOWN, probability = 0.5) {
                            block("root_strand", mod = TWILIGHT)
                        }
                    }
                }
            }
        }
    }

}