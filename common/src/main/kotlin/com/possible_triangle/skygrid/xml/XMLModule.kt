package com.possible_triangle.skygrid.xml

import com.possible_triangle.skygrid.api.events.RegisterElementEvent
import com.possible_triangle.skygrid.api.events.register
import com.possible_triangle.skygrid.api.events.subclass
import com.possible_triangle.skygrid.api.xml.DeserializationException
import com.possible_triangle.skygrid.api.xml.elements.*
import com.possible_triangle.skygrid.api.xml.elements.extras.Cardinal
import com.possible_triangle.skygrid.api.xml.elements.extras.Offset
import com.possible_triangle.skygrid.api.xml.elements.extras.Side
import com.possible_triangle.skygrid.api.xml.elements.extras.Surround
import com.possible_triangle.skygrid.api.xml.elements.filters.ExceptFilter
import com.possible_triangle.skygrid.api.xml.elements.filters.ModFilter
import com.possible_triangle.skygrid.api.xml.elements.filters.NameFilter
import com.possible_triangle.skygrid.api.xml.elements.filters.TagFilter
import com.possible_triangle.skygrid.api.xml.elements.providers.*
import com.possible_triangle.skygrid.api.xml.elements.transformers.CyclePropertyTransformer
import com.possible_triangle.skygrid.api.xml.elements.transformers.SetPropertyTransformer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerializationPolicy
import kotlin.reflect.KClass

private typealias Sub<T> = Pair<KClass<T>, KSerializer<T>>

private fun SerializersModuleBuilder.collectRegisteredElements() {
    val collected = hashMapOf<KClass<*>, MutableCollection<Sub<*>>>()

    RegisterElementEvent.EVENT.invoke(object : RegisterElementEvent {
        override fun <TParent : Any, TSub : TParent> register(
            parent: KClass<TParent>,
            subclass: KClass<TSub>,
            serializer: KSerializer<TSub>,
        ) {
            collected.getOrPut(parent, ::hashSetOf).add(subclass to serializer)
        }
    })

    fun <TParent : Any, TSub : TParent> PolymorphicModuleBuilder<TParent>.subclass(it: Sub<*>) {
        subclass(it.first as KClass<TSub>, it.second as KSerializer<TSub>)
    }

    fun <TParent : Any> registerPolymorphic(parent: KClass<TParent>, subclasses: Collection<Sub<*>>) {
        polymorphic(parent) {
            subclasses.forEach {
                subclass(it)
            }
        }
    }

    collected.forEach { (parent, subclasses) ->
        registerPolymorphic(parent, subclasses)
    }
}

fun RegisterElementEvent.registerDefaultElements() {
    register(BlockProvider::class) {
        subclass(BlockList::class)
        subclass(Fallback::class)
        subclass(Tag::class)
        subclass(SingleBlock::class)
        subclass(Reference::class)
    }

    register(Transformer::class) {
        subclass(SetPropertyTransformer::class)
        subclass(CyclePropertyTransformer::class)
    }

    register(FilterOperator::class) {
        subclass(ExceptFilter::class)
    }

    register(Extra::class) {
        subclass(Side::class)
        subclass(Cardinal::class)
        subclass(Offset::class)
        subclass(Surround::class)
    }

    register(Filter::class) {
        subclass(NameFilter::class)
        subclass(ModFilter::class)
        subclass(TagFilter::class)
    }
}

@ExperimentalXmlUtilApi
fun createXMLModule() = XML(SerializersModule {
    collectRegisteredElements()
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