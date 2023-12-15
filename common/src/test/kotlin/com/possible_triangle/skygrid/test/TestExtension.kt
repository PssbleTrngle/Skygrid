package com.possible_triangle.skygrid.test

import com.possible_triangle.skygrid.SkygridMod
import com.possible_triangle.skygrid.test.mocks.WorldMock
import net.minecraft.SharedConstants
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.Bootstrap
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

fun assertApproximately(expected: Double, actual: Double?) {
    assertNotNull(actual)
    assertEquals(expected, actual, 0.00001)
}

class TestExtension : BeforeAllCallback, BeforeEachCallback {

    override fun beforeAll(context: ExtensionContext) {
        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
        Bootstrap.validate()
        SkygridMod.init()
    }

    override fun beforeEach(context: ExtensionContext?) {
        WorldMock.reset()
    }
}

fun createBuiltinLookup() =
    HolderLookup.Provider.create(
        Stream.of(
            BuiltInRegistries.BLOCK.asLookup(),
            BuiltInRegistries.ENTITY_TYPE.asLookup(),
        )
    )
