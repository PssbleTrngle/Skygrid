package possible_triangle.skygrid.data.generation.builder.providers

import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import possible_triangle.skygrid.data.generation.builder.BasicBlocksBuilder
import possible_triangle.skygrid.data.generation.builder.ExceptFilterBuilder
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.FilterOperator
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.CyclePropertyTransformer
import possible_triangle.skygrid.data.xml.impl.Offset
import possible_triangle.skygrid.data.xml.impl.SetPropertyTransformer
import possible_triangle.skygrid.data.xml.impl.Side

abstract class BlockProviderBuilder<T : BlockProvider> {

    private val extras = arrayListOf<Extra>()
    private val transformers = arrayListOf<Transformer>()
    private val filters = arrayListOf<FilterOperator>()

    fun except(builder: ExceptFilterBuilder.() -> Unit) {
        ExceptFilterBuilder().also {
            builder(it)
            filters.add(it.build())
        }
    }

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

    fun double(probability: Double = 1.0, on: Direction = Direction.UP, builder: BasicBlocksBuilder.() -> Unit) {
        listOf(DoubleBlockHalf.LOWER, DoubleBlockHalf.UPPER).forEachIndexed { i, half ->
            side(on, offset = i + 1, probability, shared = true) {
                builder(this)
                each {
                    property(BlockStateProperties.DOUBLE_BLOCK_HALF, half)
                }
            }
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

    protected abstract fun buildWith(
        extras: List<Extra>,
        transformers: List<Transformer>,
        filters: List<FilterOperator>
    ): T

    fun build(): T {
        return buildWith(extras.toList(), transformers.toList(), filters.toList())
    }

}