package possible_triangle.skygrid.builder

import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.builder.providers.*

interface IBlocksBuilder {

    val registries: RegistryAccess

    fun add(block: BlockProviderBuilder<*>)

    fun block(block: Block, weight: Double = 1.0, builder: BlockBuilder.() -> Unit = {}): BlockBuilder {
        val blocks = registries.registryOrThrow(Registry.BLOCK_REGISTRY)
        val key = requireNotNull(blocks.getKey(block))
        return block(key.path, key.namespace, weight, builder)
    }

    fun block(
        id: String,
        mod: String = "minecraft",
        weight: Double = 1.0,
        builder: BlockBuilder.() -> Unit = {},
    ): BlockBuilder {
        return BlockBuilder(id, mod, weight, registries).also {
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
        mod: String = "minecraft",
        weight: Double = 1.0,
        expand: Boolean = false,
        random: Boolean = true,
        builder: TagBuilder.() -> Unit = {},
    ): TagBuilder {
        return TagBuilder(id, mod, weight, random, expand, registries).also {
            builder(it)
            add(it)
        }
    }

    fun list(name: String? = null, weight: Double = 1.0, builder: BlockListBuilder.() -> Unit = {}): BlockListBuilder {
        return BlockListBuilder(name, weight, registries).also {
            builder(it)
            add(it)
        }
    }

    fun fallback(
        name: String? = null,
        weight: Double = 1.0,
        builder: FallbackBuilder.() -> Unit = {},
    ): FallbackBuilder {
        return FallbackBuilder(name, weight, registries).also {
            builder(it)
            add(it)
        }
    }

    fun reference(id: String, weight: Double = 1.0, builder: ReferenceBuilder.() -> Unit = {}): ReferenceBuilder {
        return ReferenceBuilder(id, weight, registries).also {
            builder(it)
            add(it)
        }
    }

}