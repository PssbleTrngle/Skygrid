package com.possible_triangle.skygrid.xml.resources

import com.possible_triangle.skygrid.api.xml.elements.Preset
import com.possible_triangle.skygrid.xml.ReferenceContext
import com.possible_triangle.skygrid.xml.XMLResource
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer

object Presets : XMLResource<Preset>("presets", { Preset.serializer() }) {

    override fun merge(a: Preset, b: Preset): Preset {
        return b
    }

    override fun validate(value: Preset, server: MinecraftServer): Boolean {
        val references = ReferenceContext()
        val blocks = server.registryAccess().registryOrThrow(Registries.BLOCK)
        return value.provider.validate(blocks.asLookup(), references)
    }

}