package com.possible_triangle.skygrid.datagen

import com.possible_triangle.skygrid.datagen.dimensions.*
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.data.DataGenerator
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
fun DataGenerator.addSkygridProviders() {
    addProvider(::SkygridTagGenerator)

    addProvider(::Presets)
    addProvider(::Overworld)
    addProvider(::Nether)
    addProvider(::End)

    addProvider(::TwilightForest)

    addProvider(::Cave, "custom-examples")
    addProvider(::Aqua, "custom-examples")
}