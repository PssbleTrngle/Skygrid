package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.datagen.DatagenContext
import com.possible_triangle.skygrid.datagen.builder.providers.*
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

interface IBlocksBuilder {

    val context: DatagenContext

    fun add(block: BlockProviderBuilder<*>)

    fun block(block: Block, weight: Double = 1.0, builder: BlockBuilder.() -> Unit = {}): BlockBuilder {
        val key = block.builtInRegistryHolder().key().location()
        return block(key.path, key.namespace, weight, builder)
    }

    fun block(
        id: String,
        mod: String = context.defaultMod,
        weight: Double = 1.0,
        builder: BlockBuilder.() -> Unit = {},
    ): BlockBuilder {
        return BlockBuilder(context, id, mod, weight).also {
            builder(it)
            add(it)
        }
    }

    fun tag(
        tag: TagKey<Block>,
        weight: Double = 1.0,
        expand: Boolean = false,
        random: Boolean = true,
        builder: TagBuilder.() -> Unit = {},
    ): TagBuilder {
        return tag(tag.location.path, tag.location.namespace, weight, expand, random, builder)
    }

    fun tag(
        id: String,
        mod: String = context.defaultMod,
        weight: Double = 1.0,
        expand: Boolean = false,
        random: Boolean = true,
        builder: TagBuilder.() -> Unit = {},
    ): TagBuilder {
        return TagBuilder(context, id, mod, weight, random, expand).also {
            builder(it)
            add(it)
        }
    }

    fun list(name: String? = null, weight: Double = 1.0, builder: BlockListBuilder.() -> Unit = {}): BlockListBuilder {
        return BlockListBuilder(context, name, weight).also {
            builder(it)
            add(it)
        }
    }

    fun fallback(
        name: String? = null,
        weight: Double = 1.0,
        builder: FallbackBuilder.() -> Unit = {},
    ): FallbackBuilder {
        return FallbackBuilder(context, name, weight).also {
            builder(it)
            add(it)
        }
    }

    fun reference(id: String, weight: Double = 1.0, builder: ReferenceBuilder.() -> Unit = {}): ReferenceBuilder {
        return ReferenceBuilder(context, id, weight).also {
            builder(it)
            add(it)
        }
    }

}