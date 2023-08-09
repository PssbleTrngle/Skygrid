package com.possible_triangle.skygrid.forge.platform

import com.possible_triangle.skygrid.platform.services.EventCallback
import com.possible_triangle.skygrid.platform.services.EventInvoker
import com.possible_triangle.skygrid.platform.services.IEvents
import net.minecraftforge.eventbus.api.GenericEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import kotlin.reflect.KClass

class ForgeEvents : IEvents {

    private class ForgeEvent<TEvent : Any>(val event: TEvent, type: KClass<TEvent>) : GenericEvent<TEvent>(type.java)

    override fun <TEvent : Any> createEvent(clazz: KClass<TEvent>): EventInvoker<TEvent> {
        return object : EventInvoker<TEvent> {
            override fun invoke(event: TEvent) {
                FORGE_BUS.post(ForgeEvent(event, clazz))
            }

            override fun addListener(callback: EventCallback<TEvent>) {
                FORGE_BUS.addGenericListener(clazz.java) { event: ForgeEvent<TEvent> ->
                    callback.accept(event.event)
                }
            }
        }
    }

}