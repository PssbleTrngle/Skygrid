package com.possible_triangle.skygrid.fabric.datagen

import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.datagen.SkygridTagGenerator
import com.possible_triangle.skygrid.datagen.dimensions.*
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceLocation
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
object FabricDataGenerators : DataGeneratorEntrypoint {

    fun FabricDataGenerator.addSkygridProviders() {
        fun FabricDataGenerator.addProvider(
            factory: (Path, CompletableFuture<HolderLookup.Provider>) -> DataProvider,
            datapack: String? = null,
        ) {
            val pack = datapack?.let { createBuiltinResourcePack(ResourceLocation(SkygridConstants.MOD_ID, it)) }
                ?: createPack()
            pack.addProvider { output, lookup -> factory(output.outputFolder, lookup) }
        }

        addProvider(::Presets)
        addProvider(::Overworld)
        addProvider(::Nether)
        addProvider(::End)

        addProvider(::TwilightForest, "twilight-forest")

        addProvider(::Cave, "custom-examples")
        addProvider(::Aqua, "custom-examples")
    }

    override fun onInitializeDataGenerator(dataGenerator: FabricDataGenerator) {
        dataGenerator.addSkygridProviders()
        dataGenerator.createPack().addProvider(::SkygridTagGenerator)
    }

}