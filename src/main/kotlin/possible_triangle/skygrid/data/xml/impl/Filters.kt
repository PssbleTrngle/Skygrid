package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.Filter

@Serializable
@SerialName("name")
data class NameFilter(val pattern: String) : Filter() {

    @Transient
    private val regex = pattern.toRegex()

    override fun test(block: Block, blocks: Registry<Block>, tags: TagContainer): Boolean {
        return regex.containsMatchIn(block.registryName.toString())
    }

}

@Serializable
@SerialName("mod")
data class ModFilter(val id: String) : Filter() {

    override fun test(block: Block, blocks: Registry<Block>, tags: TagContainer): Boolean {
        return block.registryName?.namespace == id
    }

}

@Serializable
@SerialName("tag")
data class TagFilter(val id: String, val mod: String = "minecraft") : Filter() {

    private val key
        get() = ResourceLocation(mod, if (id.startsWith("#")) id.substring(1) else id)

    override fun test(block: Block, blocks: Registry<Block>, tags: TagContainer): Boolean {
        return tags.getOrEmpty(Registry.BLOCK_REGISTRY).getTagOrEmpty(key).contains(block)
    }

}