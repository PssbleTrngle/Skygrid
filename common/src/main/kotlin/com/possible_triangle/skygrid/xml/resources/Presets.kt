package com.possible_triangle.skygrid.xml.resources

import com.possible_triangle.skygrid.api.xml.elements.Preset
import net.minecraft.core.Registry
import net.minecraft.server.MinecraftServer
import com.possible_triangle.skygrid.xml.ReferenceContext
import com.possible_triangle.skygrid.xml.XMLResource

object Presets : XMLResource<Preset>("presets", { Preset.serializer() }) {

    override fun merge(a: Preset, b: Preset): Preset {
        return b
    }

    override fun validate(value: Preset, server: MinecraftServer): Boolean {
        val references = ReferenceContext()
        val blocks = server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY)
        return value.provider.validate(blocks, references)
    }

}