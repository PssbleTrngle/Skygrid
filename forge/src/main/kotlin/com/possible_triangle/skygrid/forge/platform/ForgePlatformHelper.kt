package com.possible_triangle.skygrid.forge.platform

import com.possible_triangle.skygrid.forge.SkygridForge.BLOCKS
import com.possible_triangle.skygrid.platform.services.IPlatformHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject
import java.util.stream.Stream
import javax.annotation.Nonnull
import kotlin.jvm.optionals.getOrNull
import kotlin.properties.ReadOnlyProperty

class ForgePlatformHelper : IPlatformHelper {

    override val isDevelopmentEnvironment: Boolean
        get() = !FMLLoader.isProduction()

    override fun getTags(block: Block): Stream<ResourceLocation> {
        return ForgeRegistries.BLOCKS.tags()?.getReverseTag(block)?.map { tag ->
            tag.tagKeys.map { it.location }
        }?.getOrNull() ?: Stream.empty()
    }

    @Nonnull
    override fun createBlock(id: String, block: () -> Block): ReadOnlyProperty<Any?, Block> {
        return BLOCKS.registerObject(id, block)
    }
}