package com.possible_triangle.skygrid.forge.datagen

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import com.possible_triangle.skygrid.datagen.addSkygridProviders

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object ForgeDataGenerators {

    @SubscribeEvent
    fun register(event: GatherDataEvent) {
        if (event.includeServer()) {
            event.generator.addSkygridProviders()
        }
    }

}
