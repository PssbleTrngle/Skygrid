package possible_triangle.skygrid

import net.minecraft.resources.ResourceLocation

fun keyFrom(id: String, mod: String? = null): ResourceLocation {
    val normalizedId = if (id.startsWith("#")) id.substring(1) else id
    return if (mod == null) ResourceLocation(normalizedId)
    else ResourceLocation(mod, normalizedId)
}