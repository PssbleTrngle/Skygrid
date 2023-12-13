package com.possible_triangle.skygrid.fabric.datagen

import com.possible_triangle.skygrid.datagen.addProvider
import com.possible_triangle.skygrid.datagen.addSkygridProviders
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
object FabricDataGenerators : DataGeneratorEntrypoint {

    override fun onInitializeDataGenerator(dataGenerator: FabricDataGenerator) {
        val pack = dataGenerator.createPack()
        dataGenerator.addSkygridProviders { factory: (PackOutput, CompletableFuture<HolderLookup.Provider>) -> DataProvider ->
            { _: Path ->
                pack.addProvider(factory)
            }
        }
    }

}