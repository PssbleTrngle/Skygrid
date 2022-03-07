package possible_triangle.skygrid.data.generation

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.data.generation.dimensions.*

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object DataGenerators {

    @SubscribeEvent
    fun register(event: GatherDataEvent) {
        event.includeDev()

        fun DataGenerator.addProvider(factory: (DataGenerator) -> DataProvider) {
            addProvider(factory(this))
        }

        fun DataGenerator.addProvider(
            factory: (DataGenerator) -> DimensionConfigGenerator,
            datapack: String?,
        ) {
            factory(this).also {
                it.datapack = datapack
                addProvider(it)
            }
        }

        fun DataGenerator.addProvider(factory: (DataGenerator, ExistingFileHelper) -> DataProvider) {
            addProvider(factory(this, event.existingFileHelper))
        }

        if (event.includeServer()) {
            event.generator.addProvider(::SkygridTags)
            event.generator.addProvider(::Presets)
            event.generator.addProvider(::Overworld)
            event.generator.addProvider(::Nether)
            event.generator.addProvider(::End)

            event.generator.addProvider(::Cave, "custom-examples")
            event.generator.addProvider(::Aqua, "custom-examples")
        }
    }

}
