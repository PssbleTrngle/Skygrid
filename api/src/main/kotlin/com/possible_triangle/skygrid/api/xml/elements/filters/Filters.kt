package com.possible_triangle.skygrid.api.xml.elements.filters

import com.possible_triangle.skygrid.api.extensions.keyFrom
import com.possible_triangle.skygrid.api.xml.elements.Filter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

@Serializable
@SerialName("name")
data class NameFilter(val pattern: String) : Filter() {

    @Transient
    private val regex = pattern.toRegex()

    override fun test(block: Block, blocks: HolderLookup.RegistryLookup<Block>): Boolean {
        val key = block.builtInRegistryHolder().key().location()
        return regex.containsMatchIn(key.toString())
    }

}

@Serializable
@SerialName("mod")
data class ModFilter(val id: String) : Filter() {

    override fun test(block: Block, blocks: HolderLookup.RegistryLookup<Block>): Boolean {
        val key = block.builtInRegistryHolder().key().location()
        return key.namespace == id
    }

}

@Serializable
@SerialName("tag")
data class TagFilter(val id: String, val mod: String = "minecraft") : Filter() {

    @Transient
    private val location = keyFrom(id, mod)

    @Transient
    private val key = TagKey.create(Registries.BLOCK, location)

    override fun test(block: Block, blocks: HolderLookup.RegistryLookup<Block>): Boolean {
        return blocks.get(key)
            .map { list -> list.map { it.value() } }
            .filter { it.contains(block) }.isPresent
    }

}