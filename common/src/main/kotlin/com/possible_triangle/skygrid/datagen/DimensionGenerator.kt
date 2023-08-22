package com.possible_triangle.skygrid.datagen

import com.google.common.hash.HashCode
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import com.possible_triangle.skygrid.SkygridMod
import com.possible_triangle.skygrid.datagen.builder.DimensionBuilder
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.BiomeSource
import java.nio.file.Path

private fun JsonObject.addProperty(key: String, value: ResourceLocation) =
    addProperty(key, value.toString())

class DimensionGenerator(
    private val name: String,
    private val output: Path,
    private val context: DatagenContext,
) : DataProvider {

    private val typeGenerator = DimensionTypeGenerator(name, output)

    private val VALUES = hashMapOf<ResourceLocation, DimensionBuilder.Result>()
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    fun register(id: ResourceLocation, builder: DimensionBuilder.() -> Unit) {
        VALUES[id] = DimensionBuilder({
            typeGenerator.register(id, it)
        }, context).apply(builder).build()
    }

    override fun run(cache: CachedOutput) {
        VALUES.forEach { (id, entry) ->
            val file = output.resolve("data/${id.namespace}/dimension/${id.path}.json")

            val json = JsonObject().apply {
                addProperty("type", entry.type ?: id)
                add("generator", JsonObject().apply {
                    addProperty("type", SkygridMod.GENERATOR_KEY.location())
                    addProperty("config", entry.config ?: id)

                    val biomeSourceJson =
                        BiomeSource.CODEC.encodeStart(JsonOps.INSTANCE, entry.biomeSource).result().orElseThrow()
                    add("biome_source", biomeSourceJson)
                })
            }

            val data = GSON.toJson(json).encodeToByteArray()
            val hash = HashCode.fromBytes(data)
            cache.writeIfNeeded(file, data, hash)
        }

        typeGenerator.run(cache)
    }

    override fun getName() = "Dimension $name"
}
