package com.possible_triangle.skygrid.xml

import com.possible_triangle.skygrid.SkygridMod.LOGGER
import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.api.xml.DeserializationException
import com.possible_triangle.skygrid.api.xml.elements.*
import com.possible_triangle.skygrid.api.xml.elements.impl.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerializationPolicy
import java.io.File

abstract class XMLResource<T>(val path: String, private val serializer: () -> KSerializer<T>) :
    SimplePreparableReloadListener<Map<ResourceLocation, T>>() {

    @ExperimentalXmlUtilApi
    @ExperimentalSerializationApi
    companion object {
        val LOADER = XML(SerializersModule {
            polymorphic(BlockProvider::class) {
                subclass(BlockList::class)
                subclass(Fallback::class)
                subclass(Tag::class)
                subclass(SingleBlock::class)
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
                subclass(Cardinal::class)
                subclass(Offset::class)
            }

            polymorphic(Filter::class) {
                subclass(NameFilter::class)
                subclass(ModFilter::class)
                subclass(TagFilter::class)
            }
        }) {
            defaultPolicy {
                indentString = " ".repeat(3)
                autoPolymorphic = true
                encodeDefault = XmlSerializationPolicy.XmlEncodeDefault.NEVER
                unknownChildHandler = UnknownChildHandler { input, _, descriptor, name, candidates ->
                    throw DeserializationException(
                        input.locationInfo,
                        "${descriptor.tagName}/${name ?: "<CDATA>"}",
                        candidates
                    )
                }
            }
        }

        private val RESOURCES = arrayListOf<XMLResource<*>>()

        fun reload(server: MinecraftServer) {
            RESOURCES.forEach { it.validate(server) }
        }

        fun register(register: (XMLResource<*>) -> Unit) {
            RESOURCES.forEach(register)
        }

        fun clear() {
            RESOURCES.forEach { it.values = mapOf() }
        }

    }

    open fun onReload(server: MinecraftServer) {}

    private var values = mapOf<ResourceLocation, T>()

    val keys
        get() = values.keys

    val entries
        get() = values.entries

    operator fun get(id: ResourceLocation): T? {
        return values[id]
    }

    fun exists(id: ResourceLocation): Boolean {
        return values.containsKey(id)
    }

    fun register() {
        RESOURCES.add(this)
    }

    private fun validate(server: MinecraftServer) {
        values = values.filterValues { validate(it, server) }
        onReload(server)
    }

    override fun prepare(manager: ResourceManager, profiler: ProfilerFiller): Map<ResourceLocation, T> {
        val resources = manager.listResourceStacks("${SkygridConstants.MOD_ID}/$path") { it.path.endsWith(".xml") }
        LOGGER.info("Found ${resources.size} skygrid $path")
        return resources.mapValues { load(it.key, it.value) }
            .filterValues { it != null }
            .mapValues { it.value as T }
            .mapKeys { ResourceLocation(it.key.namespace, File(it.key.path).nameWithoutExtension) }
    }

    override fun apply(loaded: Map<ResourceLocation, T>, manager: ResourceManager, profiler: ProfilerFiller) {
        LOGGER.info("Loaded ${loaded.size} skygrid $path")
        values = loaded
    }

    abstract fun merge(a: T, b: T): T

    abstract fun validate(value: T, server: MinecraftServer): Boolean

    private fun attemptDecode(id: ResourceLocation, string: String): T? {
        return try {
            LOADER.decodeFromString(serializer(), string)
        } catch (e: DeserializationException) {
            LOGGER.warn("Error loading resource '$id': Unknown field at ${e.location}: '${e.field}'")
            null
        } catch (e: Exception) {
            LOGGER.warn("Error loading resource '$id': ${e.message}")
            null
        }
    }

    private fun load(name: ResourceLocation, resources: List<Resource>): T? {
        return resources.asSequence()
            .map { it.open() }
            .map { it.reader() }
            .map { it.readLines().joinToString(separator = "\n") }
            .mapNotNull { attemptDecode(name, it) }
            .reduceOrNull(::merge)
    }

}