package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.Filter
import possible_triangle.skygrid.keyFrom

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
    private val key = TagKey.create(Registry.BLOCK_REGISTRY, location)

    override fun test(block: Block, blocks: Registry<Block>): Boolean {
        return blocks.getTagOrEmpty(key).map { it.value() }.contains(block)
    }

}