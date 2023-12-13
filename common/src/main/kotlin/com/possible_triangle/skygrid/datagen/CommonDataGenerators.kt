package com.possible_triangle.skygrid.datagen

import com.possible_triangle.skygrid.api.SkygridConstants.MOD_ID
import com.possible_triangle.skygrid.datagen.dimensions.*
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
fun DataGenerator.addSkygridProviders(
    generate: (factory: (PackOutput, CompletableFuture<HolderLookup.Provider>) -> DataProvider)
    -> (path: Path)
    -> DataProvider
) {

    addProvider(generate(::SkygridTagGenerator))

    addProvider(::Presets)
    addProvider(::Overworld)
    addProvider(::Nether)
    addProvider(::End)

    addProvider(::TwilightForest, "twilight-forest")

    addProvider(::Cave, "custom-examples")
    addProvider(::Aqua, "custom-examples")
}