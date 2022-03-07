package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Aqua(generator: DataGenerator) : DimensionConfigGenerator("aqua", generator) {

    override val datapack = "custom-examples"

    override fun generate() {

        dimension(ResourceLocation(SkygridMod.MOD_ID, "aqua")) {
            gap("water")

            blocks {
                reference("ocean")
            }
        }

    }

}