package com.possible_triangle.skygrid.test.mocks

import com.possible_triangle.skygrid.platform.services.IConfig

class ConfigMock : IConfig {
    override val showBlockTags = false
    override val showProbabilities = false
}