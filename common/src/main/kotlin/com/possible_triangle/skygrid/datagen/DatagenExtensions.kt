package com.possible_triangle.skygrid.datagen

import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import java.nio.file.Path

fun DataGenerator.addProvider(
    factory: (Path) -> DataProvider,
    datapack: String? = null,
) {
    val outputFolder = Path.of("generated")
    val output = datapack?.let { outputFolder.resolve("datapacks/${it}") } ?: outputFolder
    this.getBuiltinDatapack(true, output.toString()).addProvider { factory(it.outputFolder) }
}
