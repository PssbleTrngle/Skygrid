package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.datagen.CompatMods
import com.possible_triangle.skygrid.datagen.GridConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.PointedDripstoneBlock
import net.minecraft.world.level.block.state.properties.DripstoneThickness
import net.minecraft.world.level.material.Fluids
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Cave(output: Path) : GridConfigGenerator("cave", output) {

    override fun generate() {

        gridConfig(ResourceLocation(SkygridConstants.MOD_ID, "cave")) {
            withDimension {
                multipleBiomeSource(
                    Biomes.DRIPSTONE_CAVES,
                    Biomes.LUSH_CAVES,
                )

                type {
                    respawnAnchorWorks = true
                    bedWorks = false
                    hasSkyLight = false
                    hasCeiling = true
                    fixedTime = 18000
                    height = 256
                }
            }

            distance = Distance.of(3)
            blocks {
                reference("overworld_ores", weight = 0.1)
                list(weight = 0.1) {
                    reference("dripstone")
                    block(Blocks.DRIPSTONE_BLOCK, weight = 0.2) {
                        side(Direction.DOWN) {
                            block(Blocks.POINTED_DRIPSTONE) {
                                property(PointedDripstoneBlock.THICKNESS, DripstoneThickness.TIP_MERGE)
                                property(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN)
                                side(Direction.DOWN) {
                                    block(Blocks.POINTED_DRIPSTONE) {
                                        property(PointedDripstoneBlock.THICKNESS, DripstoneThickness.TIP_MERGE)
                                        property(PointedDripstoneBlock.TIP_DIRECTION, Direction.UP)
                                    }
                                }
                            }
                        }
                    }
                }
                reference("moss", weight = 0.05)
                block(Blocks.OBSIDIAN, weight = 0.1)
                list("fluids", weight = 0.05) {
                    block(Blocks.LAVA)
                    block(Blocks.WATER)
                }

                reference("overworld_stone") {
                    side(Direction.DOWN, probability = 0.05) {
                        block("hanging_cobweb", mod = CompatMods.BOP)
                    }
                }

                reference("sculk", weight = 0.1)
            }
        }

    }

}