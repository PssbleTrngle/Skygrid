package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.datagen.CompatMods.TWILIGHT
import com.possible_triangle.skygrid.datagen.GridConfigGenerator
import com.possible_triangle.skygrid.datagen.builder.IBlocksBuilder
import com.possible_triangle.skygrid.datagen.builder.providers.BlockBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.core.Direction.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.PipeBlock.PROPERTY_BY_DIRECTION
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS
import net.minecraft.world.level.material.Fluids
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class TwilightForest(output: Path) : GridConfigGenerator("twilight_forest", output) {

    private val woods = mapOf(
        "twilight_oak" to 1.0,
        "canopy" to 1.0,
        "dark" to 1.0,
        "time" to 0.1,
        "transformation" to 0.1,
        "mining" to 0.1,
        "sorting" to 0.1,
    )

    private fun IBlocksBuilder.thorns(id: String, weight: Double = 1.0, withRose: Boolean = false): BlockBuilder {
        return block(id, weight = weight) {
            Direction.values().forEach {
                side(it, probability = 0.2) {
                    if (withRose && it == UP) block("thorn_rose")
                    block(id) {
                        property(AXIS, it.axis)
                        property(PROPERTY_BY_DIRECTION[it.opposite]!!, true)
                    }
                }
            }
        }
    }

    override fun generate() {
        gridConfig(ResourceLocation(TWILIGHT, "twilight_forest"), defaultMod = TWILIGHT) {
            withDimension {
                type = ResourceLocation(TWILIGHT, "twilight_forest_type")
                fixedBiomeSource(Biomes.FOREST)
            }

            loot {
                table("structures/basement")
                table("structures/foundation_basement")
                table("structures/tree_cache")
                table("structures/graveyard", weight = 0.1)

                table("structures/hedge_maze")

                table("structures/tower_library", weight = 0.5)
                table("structures/tower_room", weight = 0.25)

                table("structures/hill_1", weight = 0.5)
                table("structures/hill_2", weight = 0.375)
                table("structures/hill_3", weight = 0.25)

                table("structures/troll_garden", weight = 0.2)
                table("structures/troll_vault", weight = 0.05)
                table("structures/troll_vault_with_lamp", weight = 0.02)

                table("structures/labyrinth_dead_end", weight = 0.2)
                table("structures/labyrinth_room", weight = 0.1)
                table("structures/labyrinth_vault", weight = 0.05)
                table("structures/labyrinth_vault_jackport", weight = 0.01)

                table("structures/stronghold_room", weight = 0.1)
                table("structures/stronghold_cache", weight = 0.05)

                table("structures/darktower_cache", weight = 0.1)
                table("structures/darktower_key", weight = 0.05)

                table("structures/aurora_room", weight = 0.01)
                table("structures/aurora_cache", weight = 0.005)

                table("structures/well")
                table("structures/fance_well", weight = 0.1)
            }

            blocks {
                list("ground") {
                    block(Blocks.GRASS_BLOCK) {
                        side(UP) {
                            block(Blocks.GRASS)
                            block(Blocks.FERN)

                            block("fiddlehead")
                            block("mayapple")

                            list("saplings") {
                                woods.forEach { (wood, weight) ->
                                    block("${wood}_sapling", weight = weight)
                                }
                            }
                        }
                    }

                    block(Blocks.DIRT, weight = 0.1) {
                        side(DOWN, probability = 0.8) {
                            block("torchberry_plant")
                        }
                    }

                    block(Blocks.MYCELIUM, weight = 0.1) {
                        side(UP, probability = 0.5) {
                            block(Blocks.BROWN_MUSHROOM)
                            block(Blocks.RED_MUSHROOM)
                        }
                    }

                    block(Blocks.STONE) {
                        side(UP, probability = 0.2) {
                            block("mushgloom")
                        }
                    }

                    reference("overworld_ores", weight = 0.2) {
                        except {
                            pattern("deepslate_")
                        }
                    }
                }

                list("wood", weight = 0.5) {
                    cycle(AXIS)

                    list("logs") {
                        list("full logs") {
                            woods.forEach { (wood, weight) ->
                                block("${wood}_log", weight = weight)
                            }
                            cardinal(probability = 0.1) {
                                property(BlockStateProperties.FACING, NORTH)
                                block("firefly")
                                block("cicada")
                            }
                        }

                        list("hollow logs", weight = 0.2) {
                            woods.forEach { (wood, weight) ->
                                block("hollow_${wood}_log_horizontal", weight = weight)
                            }
                            side(UP, probability = 0.5) {
                                block("moss_patch")
                            }
                        }
                    }

                    list("leaves") {
                        woods.forEach { (wood, weight) ->
                            block("${wood}_leaves", weight = weight)
                        }
                    }

                    list("mushroom blocks", weight = 0.1) {
                        block(Blocks.BROWN_MUSHROOM_BLOCK)
                        block(Blocks.RED_MUSHROOM_BLOCK)
                    }

                    list("roots", weight = 0.25) {
                        block("root")
                        block("liveroot_block", weight = 0.1)
                        side(DOWN, probability = 0.5) {
                            block("root_strand")
                        }
                    }

                    list("thorns", weight = 0.1) {
                        thorns("brown_thorns")
                        thorns("green_thorns", withRose = true)
                    }
                }

                list("building", weight = 0.1) {
                    list("bookshelves") {
                        block("canopy_bookshelf")
                    }
                }

                list("storage", weight = 0.01) {
                    block("steeleaf_block")
                    block("ironwood_block")
                    block("fiery_block", weight = 0.1)
                    block("knightmetal_block", weight = 0.1)
                    block("carminite_block", weight = 0.05)
                    block("arctic_fur_block", weight = 0.05)
                }

                list("fluids", weight = 0.1) {
                    fluid(Fluids.WATER) {
                        side(UP, probability = 0.3) {
                            block(Blocks.LILY_PAD)
                        }
                    }
                }

                list("loot", weight = 0.05) {
                    list("chests") {
                        cycle(ChestBlock.FACING)
                        woods.forEach { (wood, weight) ->
                            block("${wood}_chest", weight = weight)
                        }
                    }
                }
            }
        }
    }

}