package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.datagen.DimensionConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Cave(generator: DataGenerator) : DimensionConfigGenerator("cave", generator) {

    override fun generate() {

        dimension(ResourceLocation(SkygridConstants.MOD_ID, "cave")) {
            distance = Distance.of(3)
            blocks {
                reference("ores", weight = 0.1)
                reference("dripstone", weight = 0.1)
                reference("moss", weight = 0.05)
                block(Blocks.OBSIDIAN, weight = 0.1)
                list("fluids", weight = 0.05) {
                    block(Blocks.LAVA)
                    block(Blocks.WATER)
                }
                tag(BlockTags.BASE_STONE_OVERWORLD) {
                    side(Direction.UP, probability = 0.1) {
                        block("glow_shroom", "quark")
                    }
                    side(Direction.DOWN, probability = 0.05) {
                        block("hanging_cobweb", mod = "biomesoplenty")
                    }
                }
            }
        }

    }

}