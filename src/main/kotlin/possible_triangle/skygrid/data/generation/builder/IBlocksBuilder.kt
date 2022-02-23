package possible_triangle.skygrid.data.generation.builder

import net.minecraft.core.Direction
import net.minecraft.tags.Tag
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import possible_triangle.skygrid.data.generation.builder.providers.BlockBuilder
import possible_triangle.skygrid.data.generation.builder.providers.BlockListBuilder
import possible_triangle.skygrid.data.generation.builder.providers.TagBuilder
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.impl.Reference

fun interface IBlocksBuilder {

    fun add(block: BlockProvider)

    fun double(block: Block, weight: Double = 1.0, builder: BlockBuilder.() -> Unit = {}) {
        val key = block.registryName
        require(key != null)
        return double(key.path, key.namespace, weight, builder)
    }

    fun double(id: String, mod: String, weight: Double, builder: BlockBuilder.() -> Unit = {}) {
        block(id, mod, weight) {
            builder(this)
            property(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            side(Direction.UP) {
                block(id, mod) {
                    property(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                }
            }
        }
    }

    fun double(tag: Tag.Named<Block>, weight: Double = 1.0, builder: TagBuilder.() -> Unit = {}) {
        tag(tag, weight) {
            builder(this)
            property(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            side(Direction.UP, shared = true) {
                tag(tag) {
                    property(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
                }
            }
        }
    }

    fun block(block: Block, weight: Double = 1.0, builder: BlockBuilder.() -> Unit = {}) {
        val key = block.registryName
        require(key != null)
        return block(key.path, key.namespace, weight, builder)
    }

    fun block(id: String, mod: String = "minecraft", weight: Double = 1.0, builder: BlockBuilder.() -> Unit = {}) {
        BlockBuilder(id, mod).also {
            builder(it)
            add(it.build(weight))
        }
    }

    fun tag(
        tag: Tag.Named<Block>,
        weight: Double = 1.0,
        expand: Boolean = false,
        random: Boolean = true,
        builder: TagBuilder.() -> Unit = {},
    ) {
        return tag(tag.name.path, tag.name.namespace, weight, expand, random, builder)
    }

    fun tag(
        id: String,
        mod: String = "minecraft",
        weight: Double = 1.0,
        expand: Boolean = false,
        random: Boolean = true,
        builder: TagBuilder.() -> Unit = {},
    ) {
        TagBuilder(id, mod, random, expand).also {
            builder(it)
            add(it.build(weight))
        }
    }

    fun list(name: String? = null, weight: Double = 1.0, builder: BlockListBuilder.() -> Unit = {}) {
        BlockListBuilder(name).also {
            builder(it)
            add(it.build(weight))
        }
    }

    fun reference(id: String, weight: Double = 1.0) {
        add(Reference(id, weight))
    }

}