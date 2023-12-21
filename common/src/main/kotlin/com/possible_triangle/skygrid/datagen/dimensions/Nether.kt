package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.datagen.CompatMods.BOP
import com.possible_triangle.skygrid.datagen.CompatMods.BOTANIA
import com.possible_triangle.skygrid.datagen.CompatMods.QUARK
import com.possible_triangle.skygrid.datagen.GridConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction.DOWN
import net.minecraft.core.Direction.UP
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.SkullBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS
import net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.material.Fluids
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Nether(output: Path) : GridConfigGenerator("nether", output) {

    override fun generate() {

        gridConfig(LevelStem.NETHER) {
            mobs {
                mob(EntityType.BLAZE)
                mob(EntityType.MAGMA_CUBE, weight = 0.3)
            }

            blocks {
                list("fluids", weight = 0.1) {
                    fluid(Fluids.LAVA, weight = 0.5)
                    fluid("blood", mod = BOP)
                }

                list("ground") {
                    block(Blocks.NETHERRACK) {
                        side(UP, probability = 0.1) {
                            block("burning_blossom", mod = BOP)
                            block("hellbark_sapling", mod = BOP)
                            block(Blocks.CRIMSON_FUNGUS)
                            block(Blocks.WARPED_FUNGUS)
                            block("bramble", mod = BOP) {
                                property(BlockStateProperties.DOWN, true)
                            }
                        }
                    }
                    block(Blocks.NETHERRACK, weight = 0.1) {
                        side(UP) {
                            block(Blocks.FIRE)
                        }
                    }
                    block(Blocks.MAGMA_BLOCK, weight = 0.5)
                    block(Blocks.GRAVEL, weight = 0.5)
                    block(Blocks.BASALT, weight = 0.5) { cycle(AXIS) }
                    block(Blocks.BLACKSTONE) {
                        side(UP, probability = 0.2) {
                            block("blackstone_spines", mod = BOP)
                        }
                    }

                    list("soul") {
                        block(Blocks.SOUL_SAND)
                        block(Blocks.SOUL_SOIL)
                        side(UP, probability = 0.1) {
                            block(Blocks.SOUL_FIRE)
                        }
                    }

                    block(Blocks.WARPED_NYLIUM) {
                        side(UP, probability = 0.4) {
                            block(Blocks.WARPED_ROOTS)
                            block(Blocks.NETHER_SPROUTS)
                            block(Blocks.TWISTING_VINES, weight = 0.2)
                        }
                    }

                    block(Blocks.CRIMSON_NYLIUM) {
                        side(DOWN, probability = 0.02) { block(Blocks.WEEPING_VINES) }
                        side(UP, probability = 0.1) {
                            block(Blocks.CRIMSON_ROOTS)
                        }
                    }

                    tag("flesh", mod = BOP) {
                        side(DOWN) {
                            block("flesh_tendons", mod = BOP)
                        }
                    }

                    block(Blocks.BONE_BLOCK, weight = 0.1) {
                        cycle(AXIS)
                    }

                    block("brimstone", mod = BOP, weight = 0.2) {
                        side(UP) {
                            block("brimstone_bud", mod = BOP, weight = 2.0)
                            block("brimstone_fumarole", mod = BOP)
                            block("brimstone_cluster", mod = BOP) {
                                property(DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                                side(UP) {
                                    property(DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                                    block("brimstone_cluster", mod = BOP)
                                }
                            }
                        }
                    }
                }

                list("ores", weight = 0.2) {
                    block(Blocks.NETHER_QUARTZ_ORE)
                    block(Blocks.NETHER_GOLD_ORE)
                    block(Blocks.ANCIENT_DEBRIS, weight = 0.02)
                    block(Blocks.COAL_BLOCK, weight = 0.1)
                    block(Blocks.GOLD_BLOCK, weight = 0.1)
                    block(Blocks.NETHERITE_BLOCK, weight = 0.005)
                    block(Blocks.GILDED_BLACKSTONE, weight = 0.1)
                }

                list("trees", weight = 0.1) {
                    list("logs") {
                        cycle(AXIS)
                        block(Blocks.WARPED_STEM)
                        block(Blocks.CRIMSON_STEM)
                        block("hellbark_log", mod = BOP)
                    }
                    list("leaves") {
                        block(Blocks.NETHER_WART_BLOCK)
                        block(Blocks.WARPED_WART_BLOCK)
                        block(Blocks.SHROOMLIGHT)
                        block(Blocks.RED_MUSHROOM_BLOCK, weight = 0.05)
                        block(Blocks.BROWN_MUSHROOM_BLOCK, weight = 0.05)
                    }
                }

                block(Blocks.GLOWSTONE, weight = 0.1)
                block("rose_quartz_block", mod = BOP, weight = 0.05) {
                    cardinal3D(probability = 0.5) {
                        block("rose_quartz_cluster", mod = BOP)
                    }
                }

                reference("spawner", weight = 0.01)

                list("loot", weight = 0.02) {
                    cycle(ChestBlock.FACING)
                    block("warped_chest", mod = QUARK)
                    block("crimson_chest", mod = QUARK)
                    fallback {
                        block("nether_brick_chest", mod = QUARK)
                        block(Blocks.CHEST)
                    }
                }

                list("compressed", weight = 0.01) {
                    block("nether_wart_sack", mod = QUARK)
                    block("blaze_lantern", mod = QUARK)
                    block("blaze_block", mod = BOTANIA, weight = 0.1)
                }

                list("building", weight = 0.1) {
                    cycle(AXIS)
                    block(Blocks.OBSIDIAN)
                    block(Blocks.CRYING_OBSIDIAN)
                    list("workstations", weight = 0.1) {
                        block(Blocks.RESPAWN_ANCHOR, weight = 0.5)
                        block(Blocks.SMITHING_TABLE)
                        block("blackstone_furnace", mod = QUARK)
                    }

                    list("quartz") {
                        block(Blocks.QUARTZ_BLOCK)
                        block(Blocks.QUARTZ_BRICKS)
                        block(Blocks.QUARTZ_PILLAR)
                    }

                    list("blackstone") {
                        block(Blocks.POLISHED_BLACKSTONE_BRICKS)
                        block(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
                    }

                    list("basalt") {
                        block(Blocks.POLISHED_BASALT)
                        block(Blocks.SMOOTH_BASALT)
                    }

                    list("soul sandstone") {
                        block("soul_sandstone", mod = QUARK)
                        block("chiseled_soul_sandstone", mod = QUARK)
                        block("cut_soul_sandstone", mod = QUARK)
                        block("soul_sandstone_bricks", mod = QUARK)
                    }

                    list("nether bricks") {
                        side(UP, probability = 0.05) {
                            block(Blocks.WITHER_SKELETON_SKULL) {
                                cycle(SkullBlock.ROTATION)
                            }
                        }
                        block(Blocks.NETHER_BRICKS, weight = 10.0)
                        block("blue_nether_bricks", mod = QUARK)
                        block(Blocks.CRACKED_NETHER_BRICKS)
                        block(Blocks.CHISELED_NETHER_BRICKS)
                        block(Blocks.RED_NETHER_BRICKS)
                    }
                }
            }
        }

    }

}