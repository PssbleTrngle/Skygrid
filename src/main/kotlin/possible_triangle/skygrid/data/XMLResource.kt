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
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.eventbus.api.EventPriority
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerializationPolicy
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.xml.*
import possible_triangle.skygrid.data.xml.impl.*
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import java.io.File

abstract class XMLResource<T>(private val path: String, private val serializer: () -> KSerializer<T>) :
    SimplePreparableReloadListener<Map<ResourceLocation, T>>() {

    @ExperimentalXmlUtilApi
    @ExperimentalSerializationApi
    companion object {
        val LOADER = XML(SerializersModule {
            polymorphic(BlockProvider::class) {
                subclass(BlockList::class)
                subclass(Fallback::class)
                subclass(Tag::class)
                subclass(Block::class)
                subclass(Reference::class)
            }

            polymorphic(Transformer::class) {
                subclass(SetPropertyTransformer::class)
                subclass(CyclePropertyTransformer::class)
            }

            polymorphic(FilterOperator::class) {
                subclass(ExceptFilter::class)
            }

            polymorphic(Extra::class) {
                subclass(Side::class)
                subclass(Offset::class)
            }

            polymorphic(Filter::class) {
                subclass(NameFilter::class)
                subclass(ModFilter::class)
                subclass(TagFilter::class)
            }
        }) {
            encodeDefault = XmlSerializationPolicy.XmlEncodeDefault.NEVER
            autoPolymorphic = true
            indentString = " ".repeat(3)
            unknownChildHandler = UnknownChildHandler { input, _, descriptor, name, candidates ->
                throw DeserializationException(
                    input.locationInfo,
                    "${descriptor.tagName}/${name ?: "<CDATA>"}",
                    candidates
                )
            }
        }

        private val RESOURCES = arrayListOf<XMLResource<*>>()

        fun reload(server: MinecraftServer) {
            RESOURCES.forEach { it.validate(server) }
        }

    }

    private var values = mapOf<ResourceLocation, T>()

    val keys
        get() = values.keys

    operator fun get(id: ResourceLocation): T? {
        return values[id]
    }

    fun exists(id: ResourceLocation): Boolean {
        return values.containsKey(id)
    }

    fun register() {
        RESOURCES.add(this)
        FORGE_BUS.addListener(EventPriority.HIGH) { event: AddReloadListenerEvent -> event.addListener(this) }
        FORGE_BUS.addListener { _: ServerStoppingEvent -> values = mapOf() }
        FORGE_BUS.addListener { event: ServerAboutToStartEvent -> validate(event.server) }
    }

    private fun validate(server: MinecraftServer) {
        values = values.filterValues { validate(it, server) }
    }

    override fun prepare(manager: ResourceManager, profiler: ProfilerFiller): Map<ResourceLocation, T> {
        val resources = manager.listResources("${SkygridMod.ID}/$path") { it.endsWith(".xml") }
        SkygridMod.LOGGER.info("Found ${resources.size} $path")
        return resources.associateWith { load(manager, it) }
            .filterValues { it != null }
            .mapValues { it.value as T }
            .mapKeys { ResourceLocation(it.key.namespace, File(it.key.path).nameWithoutExtension) }
    }

    override fun apply(loaded: Map<ResourceLocation, T>, manager: ResourceManager, profiler: ProfilerFiller) {
        SkygridMod.LOGGER.info("Loaded ${loaded.size} $path")
        values = loaded
    }

    abstract fun merge(a: T, b: T): T

    abstract fun validate(value: T, server: MinecraftServer): Boolean

    private fun attemptDecode(id: ResourceLocation, string: String): T? {
        return try {
            LOADER.decodeFromString(serializer(), string)
        } catch (e: DeserializationException) {
            SkygridMod.LOGGER.warn("Error loading resource '$id': Unknown field at ${e.location}: '${e.field}'")
            null
        } catch (e: Exception) {
            SkygridMod.LOGGER.warn("Error loading resource '$id': ${e.message}")
            null
        }
    }

    private fun load(manager: ResourceManager, name: ResourceLocation): T? {
        val resources = manager.getResources(name)
        return resources.asSequence()
            .map { it.inputStream }
            .map { it.reader() }
            .map { it.readLines().joinToString(separator = "\n") }
            .mapNotNull { attemptDecode(name, it) }
            .reduceOrNull(::merge)
    }

}