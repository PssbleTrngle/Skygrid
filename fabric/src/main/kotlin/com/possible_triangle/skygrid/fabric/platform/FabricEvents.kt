package com.possible_triangle.skygrid.fabric.platform

import com.possible_triangle.skygrid.platform.services.EventCallback
import com.possible_triangle.skygrid.platform.services.EventInvoker
import com.possible_triangle.skygrid.platform.services.IEvents
import net.fabricmc.fabric.api.event.EventFactory
import kotlin.reflect.KClass

class FabricEvents : IEvents {

    override fun <TEvent : Any> createEvent(clazz: KClass<TEvent>): EventInvoker<TEvent> {
        val type = EventCallback::class.java
        val fabricEvent = EventFactory.createArrayBacked<EventCallback<TEvent>>(type) { listeners ->
            EventCallback { event ->
                listeners.forEach { callback ->
                    callback.accept(event)
                }
            }
        }

        return object : EventInvoker<TEvent> {
            override fun invoke(event: TEvent) = fabricEvent.invoker().accept(event)
            override fun addListener(callback: EventCallback<TEvent>) = fabricEvent.register(callback)
        }
    }

}