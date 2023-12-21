package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.datagen.CompatMods.ENDERGETIC
import com.possible_triangle.skygrid.datagen.CompatMods.QUARK
import com.possible_triangle.skygrid.datagen.GridConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction.DOWN
import net.minecraft.core.Direction.UP
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.EndRodBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS
import net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.dimension.LevelStem
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class End(output: Path, lookup: CompletableFuture<HolderLookup.Provider>) : GridConfigGenerator("end", output, lookup) {

    override fun generate() {

        gridConfig(LevelStem.END) {
            distance = Distance.of(5)

            mobs {
                mob(EntityType.ENDERMAN)
                mob(EntityType.SHULKER, weight = 0.1)
            }

            loot {
                table("chests/end_city_treasure")
            }

            blocks {
                list("ground") {
                    block(Blocks.END_STONE) {
                        side(UP, probability = 0.1) {
                            block("chorus_weeds", mod = QUARK)
                            block("chorus_twist", mod = QUARK)
                        }
                    }
                    block(Blocks.END_STONE, weight = 0.05) {
                        side(UP) {
                            block(Blocks.CHORUS_FLOWER)
                        }
                    }
                    block("poismoss", mod = ENDERGETIC, weight = 0.5) {
                        side(UP, probability = 0.8) {
                            block("poise_bush", mod = ENDERGETIC)
                            block("tall_poise_bush", mod = ENDERGETIC, weight = 0.5) {
                                property(DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
                                side(UP) {
                                    block("tall_poise_bush", mod = ENDERGETIC) {
                                        property(DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                                    }
                                }
                            }
                        }
                    }
                }

                list("ores", weight = 0.2) {
                    block("biotite_ore", QUARK)
                }

                list("wood", weight = 0.1) {
                    block("poise_planks", mod = ENDERGETIC, weight = 0.2)
                    block("poise_stem", mod = ENDERGETIC)
                    block("poise_cluster", mod = ENDERGETIC)
                    block("glowing_poise_stem", mod = ENDERGETIC)
                }

                list("building") {
                    cycle(AXIS)
                    list("obsidian", weight = 0.1) {
                        block(Blocks.OBSIDIAN)
                        block(Blocks.CRYING_OBSIDIAN)
                        side(DOWN, probability = 0.5) {
                            block("acidian_lantern", mod = ENDERGETIC) {
                                property(EndRodBlock.FACING, DOWN)
                            }
                        }
                    }

                    list("endstone bricks") {
                        block(Blocks.END_STONE_BRICKS)
                        block("cracked_end_stone_bricks", mod = ENDERGETIC, weight = 0.5)
                        block("chiseled_end_stone_bricks", mod = ENDERGETIC, weight = 0.5)
                    }

                    list("purpur") {
                        block(Blocks.PURPUR_PILLAR)
                        block(Blocks.PURPUR_BLOCK)
                        block("cracked_purpur_block", mod = ENDERGETIC, weight = 0.5)
                    }

                    list("duskbound", weight = 0.5) {
                        block("duskbound_block", mod = QUARK)
                        block("duskbound_lantern", mod = QUARK)
                    }

                    list("myalite") {
                        block("myalite", mod = QUARK, weight = 10.0)
                        block("myalite_crystal", mod = QUARK, weight = 5.0)
                        block("dusky_myalite", mod = QUARK)
                        block("myalite_bricks", mod = QUARK)
                        block("chiseled_myalite_bricks", mod = QUARK)
                        block("myalite_pillar", mod = QUARK)
                    }

                    block(Blocks.ENDER_CHEST, weight = 0.01)
                    block("poise_bookshelf", mod = ENDERGETIC, weight = 0.01)
                }

                list("compressed", weight = 0.05) {
                    block("chorus_fruit_block", mod = QUARK)
                    block("bolloom_crate", mod = ENDERGETIC)
                    block("boof_block", mod = ENDERGETIC, weight = 0.1)
                }

                list("loot", weight = 0.01) {
                    block(Blocks.SHULKER_BOX, weight = 0.0075)
                    fallback {
                        cycle(ChestBlock.FACING)
                        list {
                            block("purpur_chest", mod = QUARK)
                            block("poise_chest", mod = ENDERGETIC)
                        }
                        block(Blocks.CHEST)
                    }
                }
            }
        }

    }

}