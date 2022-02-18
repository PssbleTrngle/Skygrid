package possible_triangle.skygrid.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.server.ServerAboutToStartEvent
import net.minecraftforge.eventbus.api.EventPriority
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerializationPolicy
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.Filter
import possible_triangle.skygrid.config.FilterOperator
import possible_triangle.skygrid.config.Transformer
import possible_triangle.skygrid.config.impl.*
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import java.io.File

@ExperimentalSerializationApi
abstract class XMLResource<T>(private val path: String, private val serializer: () -> KSerializer<T>) :
    SimplePreparableReloadListener<Map<ResourceLocation, T>>() {

    companion object {
        val MODULE = SerializersModule {
            polymorphic(BlockProvider::class) {
                subclass(BlockList::class)
                subclass(Fallback::class)
                subclass(Tag::class)
                subclass(Block::class)
                subclass(Reference::class)
            }

            polymorphic(Transformer::class) {
                subclass(PropertyTransformer::class)
            }

            polymorphic(FilterOperator::class) {
                subclass(ExceptFilter::class)
            }

            polymorphic(Filter::class) {
                subclass(NameFilter::class)
            }
        }
        val LOADER = XML(MODULE) {
            encodeDefault = XmlSerializationPolicy.XmlEncodeDefault.NEVER
            autoPolymorphic = true
            xmlDeclMode
        }
    }

    private var values = mapOf<ResourceLocation, T>()

    operator fun get(id: ResourceLocation): T? {
        return values[id]
    }

    init {
        FORGE_BUS.addListener(EventPriority.HIGH, ::register)
        FORGE_BUS.addListener(::validateAll)
    }

    private fun register(event: AddReloadListenerEvent) {
        event.addListener(this)
    }

    private fun validateAll(event: ServerAboutToStartEvent) {
        values = values.filterValues { validate(it, event.server) }
    }

    override fun prepare(manager: ResourceManager, profiler: ProfilerFiller): Map<ResourceLocation, T> {
        val resources = manager.listResources("${SkygridMod.ID}/$path") { it.endsWith(".xml") }
        return resources.associateWith { load(manager, it) }
            .mapKeys { ResourceLocation(it.key.namespace, File(it.key.path).nameWithoutExtension) }
    }

    override fun apply(loaded: Map<ResourceLocation, T>, manager: ResourceManager, profiler: ProfilerFiller) {
        values = loaded
        SkygridMod.LOGGER.info("Loaded ${values.size} dimensions")
    }

    abstract fun merge(a: T, b: T): T

    abstract fun validate(value: T, server: MinecraftServer): Boolean

    private fun load(manager: ResourceManager, name: ResourceLocation): T {
        val serializer = serializer()
        val resources = manager.getResources(name)
        return resources.asSequence()
            .map { it.inputStream }
            .map { it.bufferedReader() }
            .map { it.readLines().joinToString(separator = "\n") }
            .map { LOADER.decodeFromString(serializer, it) }
            .reduce(::merge)
    }

}