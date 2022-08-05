package com.possible_triangle.skygrid.test

import com.possible_triangle.skygrid.test.mocks.WorldMock
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestExtension : BeforeAllCallback, BeforeEachCallback {

    override fun beforeAll(context: ExtensionContext) {
        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
        Bootstrap.validate()
    }

    override fun beforeEach(context: ExtensionContext?) {
        WorldMock.reset()
    }
}