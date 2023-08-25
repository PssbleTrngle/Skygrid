package com.possible_triangle.skygrid.minigame.gamedata

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.server.MinecraftServer
import net.minecraft.world.scores.PlayerTeam

@Serializable
@SerialName("flags")
data class Flags(private val flags: MutableMap<@Contextual PlayerTeam, @Contextual BlockPos>) {
    companion object : Persisted<Flags>(Flags.serializer()) {
        override fun empty(): Flags = Flags(hashMapOf())

        fun get(server: MinecraftServer, team: PlayerTeam) = Flags.Companion[server].flags[team]

        fun place(server: MinecraftServer, team: PlayerTeam, pos: BlockPos) = modify(server) {
            it.flags[team] = pos
        }

        fun remove(server: MinecraftServer, team: PlayerTeam) = modify(server) {
            it.flags.remove(team)
        }
    }
}