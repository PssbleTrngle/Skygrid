package com.possible_triangle.skygrid.api.events

import com.possible_triangle.skygrid.platform.Services
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

interface RegisterElementEvent {

    companion object {
        val EVENT = Services.EVENTS.createEvent(RegisterElementEvent::class)
    }

    fun <TParent : Any, TSub : TParent> register(
        parent: KClass<TParent>,
        subclass: KClass<TSub>,
        serializer: KSerializer<TSub>,
    )

}

interface RegisterElementBuilder<TParent : Any> {
    fun <TSub : TParent> subclass(clazz: KClass<TSub>, serializer: KSerializer<TSub>)
}

inline fun <TParent : Any, reified TSub : TParent> RegisterElementEvent.register(
    parent: KClass<TParent>,
    subclass: KClass<TSub>,
) = register(parent, subclass, serializer())

inline fun <TParent : Any, reified TSub : TParent> RegisterElementBuilder<TParent>.subclass(clazz: KClass<TSub>) =
    subclass(clazz, serializer())


inline fun <TParent : Any> RegisterElementEvent.register(
    parent: KClass<TParent>,
    builder: RegisterElementBuilder<TParent>.() -> Unit,
) = object : RegisterElementBuilder<TParent> {
    override fun <TSub : TParent> subclass(clazz: KClass<TSub>, serializer: KSerializer<TSub>) {
        register(parent, clazz, serializer)
    }
}.apply(builder)