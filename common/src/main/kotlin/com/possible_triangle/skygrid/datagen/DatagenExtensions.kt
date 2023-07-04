package com.possible_triangle.skygrid.datagen

import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider

fun DataGenerator.addProvider(factory: (DataGenerator) -> DataProvider) {
    addProvider(true, factory(this))
}

fun DataGenerator.addProvider(
    factory: (DataGenerator) -> DimensionConfigGenerator,
    datapack: String?,
) {
    factory(this).also {
        it.datapack = datapack
        addProvider(true, it)
    }
}