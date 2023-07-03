package possible_triangle.skygrid.forge.datagen

import com.google.common.hash.HashCode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import net.minecraft.core.RegistryAccess
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.dimension.LevelStem
import possible_triangle.skygrid.builder.BasicBlocksBuilder
import possible_triangle.skygrid.builder.DimensionConfigBuilder
import possible_triangle.skygrid.builder.IBlocksBuilder
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Preset
import possible_triangle.skygrid.data.xml.impl.BlockList

@ExperimentalSerializationApi
abstract class DimensionConfigGenerator(private val name: String, private val generator: DataGenerator) : DataProvider {

    private val configs = hashMapOf<ResourceLocation, DimensionConfig>()
    private val presets = hashMapOf<ResourceLocation, Preset>()
    private val registries = RegistryAccess.BUILTIN.get()

    var datapack: String? = null

    fun dimension(key: ResourceKey<LevelStem>, builder: DimensionConfigBuilder.() -> Unit) =
        dimension(key.location(), builder)

    fun dimension(key: ResourceLocation, builder: DimensionConfigBuilder.() -> Unit) {
        DimensionConfigBuilder(registries).apply {
            builder(this)
            configs[key] = build()
        }
    }

    fun preset(key: String, builder: IBlocksBuilder.() -> Unit) = preset(ResourceLocation(key), builder)
    fun preset(key: ResourceLocation, builder: IBlocksBuilder.() -> Unit) {
        val providers = BasicBlocksBuilder(registries).also(builder).build()
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

    final override fun run(cache: CachedOutput) {
        generate()

        val directory = if (datapack == null)
            generator.outputFolder
        else
            generator.outputFolder.resolve("datapacks/${datapack}")

        fun write(type: String, key: ResourceLocation, content: String) {
            val file = directory.resolve("data/" + key.namespace + "/skygrid/$type/" + key.path + ".xml")
            val data = content.encodeToByteArray()
            val hash = HashCode.fromBytes(data)
            cache.writeIfNeeded(file, data, hash)
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