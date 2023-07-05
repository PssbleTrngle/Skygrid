package com.possible_triangle.skygrid.platform.services

import kotlin.reflect.KClass

interface EventInvoker<TEvent> {
    fun invoke(event: TEvent)
    fun addListener(callback: EventCallback<TEvent>)
}

fun interface EventCallback<T> {
    fun accept(event: T)
}

interface IEvents {

    fun <TEvent : Any> createEvent(clazz: KClass<TEvent>): EventInvoker<TEvent>

}