package possible_triangle.skygrid.data.generation.builder.providers

import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.Property
import possible_triangle.skygrid.data.generation.builder.BasicBlocksBuilder
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.CyclePropertyTransformer
import possible_triangle.skygrid.data.xml.impl.Offset
import possible_triangle.skygrid.data.xml.impl.SetPropertyTransformer
import possible_triangle.skygrid.data.xml.impl.Side

abstract class BlockProviderBuilder<T : BlockProvider> {

    private val extras = arrayListOf<Extra>()
    private val transformers = arrayListOf<Transformer>()

    fun side(
        on: Direction,
        offset: Int = 1,
        probability: Double = 1.0,
        shared: Boolean = false,
        builder: BasicBlocksBuilder.() -> Unit,
    ) {
        BasicBlocksBuilder().also {
            builder(it)
            extras.add(Side(on.name.lowercase(), it.build(), offset, probability, shared))
        }
    }

    fun offset(
        x: Int = 0,
        y: Int = 0,
        z: Int = 0,
        probability: Double = 1.0,
        shared: Boolean = false,
        builder: BasicBlocksBuilder.() -> Unit,
    ) {
        BasicBlocksBuilder().also {
            builder(it)
            extras.add(Offset(x, y, z, it.build(), probability, shared))
        }
    }

    fun <T : Comparable<T>> property(property: Property<T>, value: T) {
        property(property.name.lowercase(), property.getName(value))
    }

    fun property(key: String, value: String) {
        transformers.add(SetPropertyTransformer(key, value))
    }

    fun <T : Comparable<T>> cycle(property: Property<T>) {
        cycle(property.name.lowercase())
    }

    fun cycle(key: String) {
        transformers.add(CyclePropertyTransformer(key))
    }

    protected abstract fun buildWith(weight: Double, extras: List<Extra>, transformers: List<Transformer>): T

    fun build(weight: Double): T {
        return buildWith(weight, extras.toList(), transformers.toList())
    }

}