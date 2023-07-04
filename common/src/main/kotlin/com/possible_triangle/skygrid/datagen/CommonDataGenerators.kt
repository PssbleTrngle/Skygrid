package com.possible_triangle.skygrid.datagen

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.data.DataGenerator
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import com.possible_triangle.skygrid.datagen.dimensions.*

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
fun DataGenerator.addSkygridProviders() {
    addProvider(::SkygridTagGenerator)

    addProvider(::Presets)
    addProvider(::Overworld)
    addProvider(::Nether)
    addProvider(::End)

    addProvider(::Cave, "custom-examples")
    addProvider(::Aqua, "custom-examples")
}