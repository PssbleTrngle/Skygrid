package com.possible_triangle.skygrid.datagen.builder.providers

import net.minecraft.core.Direction
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import com.possible_triangle.skygrid.datagen.builder.BasicBlocksBuilder
import com.possible_triangle.skygrid.datagen.builder.ExceptFilterBuilder
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.extras.Cardinal
import com.possible_triangle.skygrid.api.xml.elements.extras.Offset
import com.possible_triangle.skygrid.api.xml.elements.extras.Side
import com.possible_triangle.skygrid.api.xml.elements.transformers.CyclePropertyTransformer
import com.possible_triangle.skygrid.api.xml.elements.transformers.SetPropertyTransformer
import net.minecraft.world.level.block.state.properties.EnumProperty

abstract class BlockProviderBuilder<T : BlockProvider> {

    abstract val registries: RegistryAccess

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
        BasicBlocksBuilder(registries).also {
            builder(it)
            extras.add(Side(on.name.lowercase(), it.build(), offset, probability, shared))
        }
    }

    fun cardinal(
        offset: Int = 1,
        rotate: Boolean = true,
        probability: Double = 1.0,
        shared: Boolean = false,
        builder: BasicBlocksBuilder.() -> Unit,
    ) {
        BasicBlocksBuilder(registries).also {
            builder(it)
            extras.add(Cardinal(it.build(), offset, rotate, probability, shared))
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
        BasicBlocksBuilder(registries).also {
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

    fun property(key: EnumProperty<DoubleBlockHalf>, value: String) {
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