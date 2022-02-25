package possible_triangle.skygrid.data

import net.minecraft.resources.ResourceLocation
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Preset

open class ReferenceContext {

    private fun checkCircular(key: String) {
        //TODO
    }

    private fun contextKey(key: String): String? {
        return if (key.startsWith('#')) key.substring(1) else null
    }

    protected open fun getInternal(key: String): BlockProvider? = null

    operator fun get(key: String): BlockProvider? {
        checkCircular(key)
        return contextKey(key)?.let {
            getInternal(it)
        } ?: run {
            val id = ResourceLocation(key)
            return Preset[id]?.provider
        }
    }

    fun with(provider: BlockProvider): ReferenceContext {
        val strippedProvider = provider.stripped()
        return object : ReferenceContext() {
            override fun getInternal(key: String): BlockProvider? {
                return if (key == "provider") strippedProvider else this.getInternal(key)
            }
        }
    }

    fun with(values: Map<String, BlockProvider>): ReferenceContext {
        return object : ReferenceContext() {
            override fun getInternal(key: String): BlockProvider? {
                return values[key] ?: this.getInternal(key)
            }
        }
    }

}