package com.possible_triangle.skygrid.datagen

import com.google.common.hash.HashCode
import com.google.gson.GsonBuilder
import com.possible_triangle.skygrid.datagen.builder.DimensionTypeBuilder
import com.possible_triangle.skygrid.extensions.supplyAsync
import net.minecraft.Util
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.dimension.DimensionType
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class DimensionTypeGenerator(
    private val name: String,
    private val output: Path,
    private val context: DatagenContext,
) : DataProvider {

    private val VALUES = hashMapOf<ResourceLocation, DimensionType>()
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    fun register(id: ResourceLocation, builder: DimensionTypeBuilder.() -> Unit) {
        VALUES[id] = DimensionTypeBuilder().apply(builder).build()
    }

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        return context.jsonOps.thenApply { lookup ->
            VALUES.supplyAsync { (id, type) ->
                CompletableFuture.supplyAsync({
                    val file = output.resolve("data/${id.namespace}/dimension_type/${id.path}.json")
                    val json = DimensionType.DIRECT_CODEC.encodeStart(lookup, type).result().orElseThrow()
                    val data = GSON.toJson(json).encodeToByteArray()
                    val hash = HashCode.fromBytes(data)
                    cache.writeIfNeeded(file, data, hash)
                }, Util.backgroundExecutor())
            }
        }
    }

    override fun getName() = "Dimension Type: $name"
}
