package com.possible_triangle.skygrid.xml

import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import net.minecraft.resources.ResourceLocation
import com.possible_triangle.skygrid.xml.resources.Presets

open class ReferenceContext : IReferenceContext {

    private fun checkCircular(key: String) {
        //TODO
    }

    private fun contextKey(key: String): String? {
        return if (key.startsWith('#')) key.substring(1) else null
    }

    protected open fun getInternal(key: String): BlockProvider? = null

    override operator fun get(key: String): BlockProvider? {
        checkCircular(key)
        return contextKey(key)?.let {
            getInternal(it)
        } ?: run {
            val id = ResourceLocation(key)
            return Presets[id]?.provider
        }
    }

    override fun with(provider: BlockProvider): ReferenceContext {
        val strippedProvider = provider.stripped()
        return object : ReferenceContext() {
            override fun getInternal(key: String): BlockProvider? {
                return if (key == "provider") strippedProvider else this.getInternal(key)
            }
        }
    }

    override fun with(values: Map<String, BlockProvider>): ReferenceContext {
        return object : ReferenceContext() {
            override fun getInternal(key: String): BlockProvider? {
                return values[key] ?: this.getInternal(key)
            }
        }
    }

}