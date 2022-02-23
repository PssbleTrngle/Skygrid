package possible_triangle.skygrid.data.generation

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.data.generation.dimensions.Cave
import possible_triangle.skygrid.data.generation.dimensions.Overworld

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object DataGenerator {

    @SubscribeEvent
    fun register(event: GatherDataEvent) {
        if (event.includeServer()) {
            event.generator.addProvider(Overworld(event.generator))
            event.generator.addProvider(Cave(event.generator))
        }
    }

}
