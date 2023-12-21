package com.possible_triangle.skygrid.datagen

import com.google.common.hash.HashCode
import com.possible_triangle.skygrid.api.xml.elements.GridConfig
import com.possible_triangle.skygrid.api.xml.elements.Preset
import com.possible_triangle.skygrid.api.xml.elements.providers.BlockList
import com.possible_triangle.skygrid.datagen.builder.BasicBlocksBuilder
import com.possible_triangle.skygrid.datagen.builder.DimensionBuilder
import com.possible_triangle.skygrid.datagen.builder.GridConfigBuilder
import com.possible_triangle.skygrid.datagen.builder.IBlocksBuilder
import com.possible_triangle.skygrid.extensions.supplyAsync
import com.possible_triangle.skygrid.xml.XMLResource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import net.minecraft.core.HolderLookup
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.dimension.LevelStem
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
abstract class GridConfigGenerator(
    private val name: String,
    private val output: Path,
    private val lookup: CompletableFuture<HolderLookup.Provider>
) : DataProvider {

    private val configs = hashMapOf<ResourceLocation, GridConfig>()
    private val presets = hashMapOf<ResourceLocation, Preset>()

    private val dimensionGenerator = DimensionGenerator(name, output, createContext("minecraft"))

    private fun createContext(defaultMod: String?): DatagenContext {
        return defaultMod?.let {
            DatagenContext(lookup, defaultMod)
        } ?: DatagenContext(lookup)
    }

    fun gridConfig(key: ResourceKey<LevelStem>, defaultMod: String? = null, builder: GridConfigBuilder.() -> Unit) =
        gridConfig(key.location(), defaultMod, builder)

    fun gridConfig(key: ResourceLocation, defaultMod: String? = null, builder: GridConfigBuilder.() -> Unit) {
        val buildDimension = { builder: DimensionBuilder.() -> Unit ->
            dimensionGenerator.register(key, builder)
        }

        GridConfigBuilder(createContext(defaultMod), buildDimension).apply {
            builder(this)
            configs[key] = build()
        }
    }

    fun preset(key: String, defaultMod: String? = null, builder: IBlocksBuilder.() -> Unit) =
        preset(ResourceLocation(key), defaultMod, builder)

    fun preset(key: ResourceLocation, defaultMod: String? = null, builder: IBlocksBuilder.() -> Unit) {
        val providers = BasicBlocksBuilder(createContext(defaultMod)).also(builder).build()
        require(providers.isNotEmpty())
        val provider = when (providers.size) {
            1 -> providers.first()
            else -> BlockList(children = providers)
        }
        presets[key] = Preset(provider)
    }

    fun IBlocksBuilder.preset(
        id: String,
        weight: Double = 1.0,
        defaultMod: String? = null,
        builder: IBlocksBuilder.() -> Unit,
    ) {
        preset(id, defaultMod, builder)
        this.reference(id, weight)
    }

    abstract fun generate()

    final override fun run(cache: CachedOutput): CompletableFuture<*> {
        generate()

        fun write(type: String, key: ResourceLocation, content: String) {
            val file = output.resolve("data/${key.namespace}/skygrid/$type/${key.path}.xml")
            val data = content.encodeToByteArray()
            val hash = HashCode.fromBytes(data)
            cache.writeIfNeeded(file, data, hash)
        }

        return CompletableFuture.allOf(
            configs.supplyAsync { (key, config) ->
                val serialized = XMLResource.LOADER.encodeToString(config)
                write("dimensions", key, serialized)
            },

            presets.supplyAsync { (key, config) ->
                val serialized = XMLResource.LOADER.encodeToString(config)
                write("presets", key, serialized)
            },

            dimensionGenerator.run(cache)
        )
    }

    final override fun getName() = "Skygrid Config: $name"

}