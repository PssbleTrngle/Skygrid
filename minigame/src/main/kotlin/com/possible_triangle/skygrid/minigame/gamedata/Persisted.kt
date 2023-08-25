package com.possible_triangle.skygrid.minigame.gamedata

import com.possible_triangle.skygrid.minigame.extensions.data
import com.possible_triangle.skygrid.minigame.serialization.createSerializerModule
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.minecraft.Nbt
import net.minecraft.Util
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.saveddata.SavedData

@OptIn(ExperimentalSerializationApi::class)
abstract class Persisted<T>(
    private val serializer: KSerializer<T>,
    private val key: String = serializer.descriptor.serialName,
) {

    companion object {
        private val NBT = Util.memoize { server: MinecraftServer ->
            Nbt(createSerializerModule(server))
        }
    }

    abstract fun empty(): T

    private inner class Saved(var data: T, private val nbt: Nbt) : SavedData() {

        override fun save(tag: CompoundTag): CompoundTag {
            val result = nbt.encodeToNbt(serializer, data)

            return tag.apply {
                put("encoded", result)
            }
        }
    }

    private fun Nbt.read(tag: CompoundTag): Saved {
        val data = decodeFromNbt(serializer, tag.get("encoded")!!)
        return Saved(data, this)
    }

    private fun getRaw(server: MinecraftServer): Saved {
        val nbt = NBT.apply(server)
        return server.data.computeIfAbsent({ nbt.read(it) }, { Saved(empty(), nbt) }, key)
    }

    protected operator fun get(server: MinecraftServer) = getRaw(server).data

    protected operator fun set(server: MinecraftServer, value: T) {
        getRaw(server).apply {
            data = value
            setDirty()
        }
    }

    protected fun modifyIf(server: MinecraftServer, block: (T) -> Boolean) {
        getRaw(server).apply {
            if (block(data)) setDirty()
        }
    }

    protected fun modify(server: MinecraftServer, block: (T) -> Unit) {
        modifyIf(server) {
            block(it)
            true
        }
    }

}