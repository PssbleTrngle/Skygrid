package com.possibletriangle.skygrid;

import com.possibletriangle.skygrid.random.SkygridOptions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase {

    @Override
    public String getName() {
        return "reloadgrid";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        SkygridOptions.reload();
        ConfigSkygrid.reload();
    }

}

