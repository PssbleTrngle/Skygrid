package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.keyFrom
import kotlin.random.Random

@Serializable
@SerialName("block")
data class Block(
    private val id: String,
    private val mod: String? = null,
    override val weight: Double = 1.0,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) : BlockProvider() {

    @Transient
    lateinit var block: Block

    @Transient
    private val key = keyFrom(id, mod)

    override val name: String
        get() = key.toString()

    override fun base(random: Random): Block {
        return block
    }

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer, references: ReferenceContext): Boolean {
        return blocks.getOptional(key).map {
            block = it
        }.isPresent
    }
}