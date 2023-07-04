package com.possible_triangle.skygrid.datagen.dimensions

import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.datagen.DimensionConfigGenerator
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Aqua(generator: DataGenerator) : DimensionConfigGenerator("aqua", generator) {

    override fun generate() {

        dimension(ResourceLocation(SkygridConstants.MOD_ID, "aqua")) {
            gap("water")

            blocks {
                reference("ocean")
                fallback(weight = 0.01) {
                    block("prismarine_chest", mod = "quark")
                    block(Blocks.CHEST)
                }
            }
        }

    }

}