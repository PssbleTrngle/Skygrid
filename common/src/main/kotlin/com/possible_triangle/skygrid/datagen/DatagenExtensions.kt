package com.possible_triangle.skygrid.datagen

import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import java.nio.file.Path

fun DataGenerator.addProvider(
    factory: (Path) -> DataProvider,
    datapack: String? = null,
) {
    val output = datapack?.let { outputFolder.resolve("datapacks/${it}") } ?: outputFolder
    addProvider(true, factory(output))
}