package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction.DOWN
import net.minecraft.core.Direction.UP
import net.minecraft.data.DataGenerator
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SkullBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.dimension.LevelStem
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Nether(generator: DataGenerator) : DimensionConfigGenerator("nether", generator) {

    override fun generate() {

        dimension(LevelStem.NETHER) {
            mobs {
                mob(EntityType.BLAZE)
                mob(EntityType.MAGMA_CUBE, weight = 0.3)
            }

            blocks {
                block(Blocks.LAVA, weight = 0.5)

                list("ground") {
                    block(Blocks.NETHERRACK) {
                        side(UP, probability = 0.1) {
                            block(Blocks.FIRE)
                        }
                    }
                    block(Blocks.MAGMA_BLOCK, weight = 0.5)
                    block(Blocks.GRAVEL, weight = 0.5)
                    block(Blocks.BASALT, weight = 0.5) { cycle(AXIS) }
                    block(Blocks.BLACKSTONE, weight = 0.6)

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
                    }
                    list("leaves") {
                        block(Blocks.NETHER_WART_BLOCK)
                        block(Blocks.WARPED_WART_BLOCK)
                        block(Blocks.SHROOMLIGHT)
                        block(Blocks.RED_MUSHROOM_BLOCK, weight = 0.1)
                        block(Blocks.BROWN_MUSHROOM_BLOCK, weight = 0.1)
                    }
                }

                block(Blocks.GLOWSTONE, weight = 0.1)

                list("building", weight = 0.1) {
                    cycle(AXIS)
                    block(Blocks.OBSIDIAN)
                    block(Blocks.CRYING_OBSIDIAN)
                    block(Blocks.RESPAWN_ANCHOR, weight = 0.05)
                    block(Blocks.SMITHING_TABLE, weight = 0.1)

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

                    list("nether bricks") {
                        side(UP, probability = 0.05) {
                            block(Blocks.WITHER_SKELETON_SKULL) {
                                cycle(SkullBlock.ROTATION)
                            }
                        }
                        block(Blocks.NETHER_BRICKS, weight = 10.0)
                        block(Blocks.CRACKED_NETHER_BRICKS)
                        block(Blocks.CHISELED_NETHER_BRICKS)
                        block(Blocks.RED_NETHER_BRICKS)
                    }
                }
            }
        }

    }

}