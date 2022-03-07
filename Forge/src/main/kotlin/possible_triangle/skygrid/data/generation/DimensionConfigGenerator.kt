package possible_triangle.skygrid.data.generation

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.HashCache
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.dimension.LevelStem
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.generation.builder.BasicBlocksBuilder
import possible_triangle.skygrid.data.generation.builder.DimensionConfigBuilder
import possible_triangle.skygrid.data.generation.builder.IBlocksBuilder
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Preset
import possible_triangle.skygrid.data.xml.impl.BlockList
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
abstract class DimensionConfigGenerator(private val name: String, private val generator: DataGenerator) : DataProvider {

    private val configs = hashMapOf<ResourceLocation, DimensionConfig>()
    private val presets = hashMapOf<ResourceLocation, Preset>()

    var datapack: String? = null

    fun dimension(key: ResourceKey<LevelStem>, builder: DimensionConfigBuilder.() -> Unit) =
        dimension(key.location(), builder)

    fun dimension(key: ResourceLocation, builder: DimensionConfigBuilder.() -> Unit) {
        DimensionConfigBuilder().apply {
            builder(this)
            configs[key] = build()
        }
    }

    fun preset(key: String, builder: IBlocksBuilder.() -> Unit) = preset(ResourceLocation(key), builder)
    fun preset(key: ResourceLocation, builder: IBlocksBuilder.() -> Unit) {
        val providers = BasicBlocksBuilder().also(builder).build()
        require(providers.isNotEmpty())
        val provider = when (providers.size) {
            1 -> providers.first()
            else -> BlockList(children = providers)
        }
        presets[key] = Preset(provider)
    }

    fun IBlocksBuilder.preset(id: String, weight: Double = 1.0, builder: IBlocksBuilder.() -> Unit) {
        preset(id, builder)
        this.reference(id, weight)
    }

    abstract fun generate()

    final override fun run(cache: HashCache) {
        generate()

        val directory = if (datapack == null)
            generator.outputFolder
        else
            generator.outputFolder.resolve("datapacks/${datapack}")

        fun write(type: String, key: ResourceLocation, content: String) {
            val file = directory.resolve("data/" + key.namespace + "/skygrid/$type/" + key.path + ".xml")

            val sha = DataProvider.SHA1.hashUnencodedChars(content).toString()
            if (cache.getHash(file) != sha || !file.exists()) {
                if (!file.parent.exists()) file.parent.createDirectories()
                SkygridMod.LOGGER.info("Writing $type/$key to ${file.absolutePathString()}")
                file.writeText(content)
            }

            cache.putNew(file, sha)
        }

        configs.forEach { (key, config) ->
            val serialized = XMLResource.LOADER.encodeToString(config)
            write("dimensions", key, serialized)
        }

        presets.forEach { (key, config) ->
            val serialized = XMLResource.LOADER.encodeToString(config)
            write("presets", key, serialized)
        }
    }

    final override fun getName(): String {
        return name
    }

}