package possible_triangle.skygrid.platform

import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.platform.services.IPlatformHelper
import java.util.*

object Services {

    val PLATFORM: IPlatformHelper = load(IPlatformHelper::class.java)

    private fun <T> load(clazz: Class<T>): T {
        val loadedService: T = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow { NullPointerException("Failed to load service for ${clazz.name}") }
        SkygridMod.LOGGER.debug("Loaded $loadedService for service $clazz")
        return loadedService
    }

}