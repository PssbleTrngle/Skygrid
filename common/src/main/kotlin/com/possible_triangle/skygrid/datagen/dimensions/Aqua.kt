package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.datagen.CompatMods.AQUATIC
import com.possible_triangle.skygrid.datagen.CompatMods.QUARK
import com.possible_triangle.skygrid.datagen.CompatMods.TROPICRAFT
import com.possible_triangle.skygrid.datagen.GridConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Aqua(output: Path, lookup: CompletableFuture<HolderLookup.Provider>) : GridConfigGenerator("aqua", output, lookup) {

    override fun generate() {
        gridConfig(ResourceLocation(SkygridConstants.MOD_ID, "aqua")) {
            withDimension {
                fixedBiomeSource(Biomes.OCEAN)
                type {
                    ambientLight = 1.0F
                    height = 256
                }
            }

            gap("water")

            blocks {
                reference("ocean")
                fallback(weight = 0.01) {
                    block("prismarine_chest", mod = QUARK)
                    block(Blocks.CHEST)
                }

                list("tropicraft plants", weight = 0.3) {
                    list {
                        block("matted_eel_seagrass", mod = TROPICRAFT)
                        block("eel_seagrass_block", mod = TROPICRAFT)
                        side(Direction.UP, probability = 0.5) {
                            block("eel_seagrass", mod = TROPICRAFT)
                        }
                    }
                    list {
                        block("matted_fern_seagrass", mod = TROPICRAFT)
                        block("fern_seagrass_block", mod = TROPICRAFT)
                        side(Direction.UP, probability = 0.5) {
                            block("fern_seagrass", mod = TROPICRAFT)
                        }
                    }
                    list {
                        block("matted_sickle_seagrass", mod = TROPICRAFT)
                        block("sickle_seagrass_block", mod = TROPICRAFT)
                        side(Direction.UP, probability = 0.5) {
                            block("sickle_seagrass", mod = TROPICRAFT)
                        }
                    }
                    list {
                        block("matted_noodle_seagrass", mod = TROPICRAFT)
                        block("noodle_seagrass_block", mod = TROPICRAFT)
                        side(Direction.UP, probability = 0.5) {
                            block("noodle_seagrass", mod = TROPICRAFT)
                        }
                    }
                }

                list("building", weight = 0.1) {
                    list("kelpy") {
                        block("kelpy_cobblestone", mod = AQUATIC)
                    }

                    list("wood", weight = 0.5) {
                        cycle(BlockStateProperties.AXIS)
                        block("driftwood_log", mod = AQUATIC)
                        block("river_log", mod = AQUATIC)
                    }

                    list("coralstone", weight = 0.5) {
                        cycle(BlockStateProperties.AXIS)
                        block("coralstone", mod = AQUATIC)
                        block("chiseled_coralstone", mod = AQUATIC)
                    }
                }

                list("compressed", weight = 0.05) {
                    block("kelp_block", mod = AQUATIC)
                    block("blue_pickerelweed_block", mod = AQUATIC)
                    block("purple_pickerelweed_block", mod = AQUATIC)
                    block("tooth_block", mod = AQUATIC, weight = 0.1)
                    block("scute_block", mod = AQUATIC, weight = 0.2)
                }
            }
        }

    }

}