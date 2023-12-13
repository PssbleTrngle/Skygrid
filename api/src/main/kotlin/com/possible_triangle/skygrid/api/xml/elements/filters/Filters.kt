package com.possible_triangle.skygrid.api.xml.elements.filters

import com.possible_triangle.skygrid.api.extensions.keyFrom
import com.possible_triangle.skygrid.api.xml.elements.Filter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

@Serializable
@SerialName("name")
data class NameFilter(val pattern: String) : Filter() {

    @Transient
    private val regex = pattern.toRegex()

    override fun test(block: Block, blocks: Registry<Block>): Boolean {
        val key = blocks.getKey(block) ?: return false
        return regex.containsMatchIn(key.toString())
    }

}

@Serializable
@SerialName("mod")
data class ModFilter(val id: String) : Filter() {

    override fun test(block: Block, blocks: Registry<Block>): Boolean {
        val key = blocks.getKey(block) ?: return false
        return key.namespace == id
    }

}

@Serializable
@SerialName("tag")
data class TagFilter(val id: String, val mod: String = "minecraft") : Filter() {

    @Transient
    private val location = keyFrom(id, mod)

    @Transient
    private val key = TagKey.create(BuiltInRegistries.BLOCK.key(), location)

    override fun test(block: Block, blocks: Registry<Block>): Boolean {
        return blocks.getTagOrEmpty(key).map { it.value() }.contains(block)
    }

}