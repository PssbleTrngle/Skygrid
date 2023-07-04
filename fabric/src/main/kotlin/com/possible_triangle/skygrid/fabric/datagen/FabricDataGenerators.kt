package com.possible_triangle.skygrid.fabric.datagen

import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import com.possible_triangle.skygrid.datagen.addSkygridProviders

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
object FabricDataGenerators : DataGeneratorEntrypoint {

    override fun onInitializeDataGenerator(dataGenerator: FabricDataGenerator) {
        dataGenerator.addSkygridProviders()
    }

}