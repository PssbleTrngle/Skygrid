package com.possible_triangle.skygrid.platform.services

import com.possible_triangle.skygrid.api.events.BlockNbtModifier

interface IConfigs {

    val client: IClientConfig

    interface IClientConfig {
        val showBlockTags: Boolean
        val showProbabilities: Boolean
    }

    val server: IServerConfig

    interface IServerConfig {
        val warnInvalid: Boolean
        val modifierStrategy: IModifierStrategy
    }

    fun interface IModifierStrategy {
        fun filterAndMerge(modifiers: Collection<BlockNbtModifier.Entry>): BlockNbtModifier<Boolean>
    }

}