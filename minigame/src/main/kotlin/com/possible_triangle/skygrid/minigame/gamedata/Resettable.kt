package com.possible_triangle.skygrid.minigame.gamedata

import net.minecraft.server.MinecraftServer

fun interface Resettable {

    fun reset(server: MinecraftServer)

}