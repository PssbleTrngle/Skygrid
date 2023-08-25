package com.possible_triangle.skygrid.minigame.serialization

import kotlinx.serialization.modules.SerializersModule
import net.minecraft.server.MinecraftServer
import net.minecraft.world.scores.PlayerTeam

fun createSerializerModule(server: MinecraftServer) = SerializersModule {
    contextual(PlayerTeam::class, PlayerTeamSerializer(server))
}