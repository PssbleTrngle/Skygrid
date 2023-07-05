package com.possible_triangle.skygrid.test.mocks

import com.possible_triangle.skygrid.platform.services.EventCallback
import com.possible_triangle.skygrid.platform.services.EventInvoker
import com.possible_triangle.skygrid.platform.services.IEvents
import kotlin.reflect.KClass

class EventsMock : IEvents {

    class SimpleEventBus<TEvent> : EventInvoker<TEvent> {
        private val listeners = hashSetOf<EventCallback<TEvent>>()

        override fun invoke(event: TEvent) {
            listeners.forEach {
                it.accept(event)
            }
        }

        override fun addListener(callback: EventCallback<TEvent>) {
            listeners.add(callback)
        }
    }

    override fun <TEvent : Any> createEvent(clazz: KClass<TEvent>) = SimpleEventBus<TEvent>()

}