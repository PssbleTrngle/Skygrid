package com.possible_triangle.skygrid.datagen

import com.possible_triangle.skygrid.datagen.dimensions.*
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.data.DataGenerator
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
fun DataGenerator.addSkygridProviders() {

    // TODO: I have no idea how to make this work.
    // addProvider({ path: Path -> SkygridTagGenerator(PackOutput(path), ) })

    addProvider(::Presets)
    addProvider(::Overworld)
    addProvider(::Nether)
    addProvider(::End)

    addProvider(::TwilightForest, "twilight-forest")

    addProvider(::Cave, "custom-examples")
    addProvider(::Aqua, "custom-examples")
}