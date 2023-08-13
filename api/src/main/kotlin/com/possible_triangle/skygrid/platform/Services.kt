package com.possible_triangle.skygrid.platform

import com.possible_triangle.skygrid.platform.services.IConfigs
import com.possible_triangle.skygrid.platform.services.IEvents
import com.possible_triangle.skygrid.platform.services.IPlatformHelper
import java.util.*

object Services {

    val PLATFORM = load(IPlatformHelper::class.java)
    val CONFIGS = load(IConfigs::class.java)
    val EVENTS = load(IEvents::class.java)

    private fun <T> load(clazz: Class<T>): T {
        return ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow { NullPointerException("Failed to load service for ${clazz.name}") }
    }

}