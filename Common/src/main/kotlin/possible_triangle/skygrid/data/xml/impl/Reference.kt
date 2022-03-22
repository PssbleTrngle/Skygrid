package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.xml.*
import kotlin.random.Random

@Serializable
@SerialName("reference")
class Reference(
    private val id: String,
    override val weight: Double = 1.0,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    override val filters: List<FilterOperator> = listOf(),
) :
    ProxyProvider() {

    override val name: String
        get() = id

    @Transient
    private lateinit var provider: BlockProvider

    override fun flat(): List<Pair<Block, Double>> = this.provider.flat()

    override fun internalValidate(
        blocks: Registry<Block>,
        references: ReferenceContext,
        filters: List<FilterOperator>
    ): Boolean {
        return references[id]?.also {
            provider = it
        } != null
    }

    override fun get(random: Random): BlockProvider {
        return provider
    }
}