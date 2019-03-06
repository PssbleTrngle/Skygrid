package com.possibletriangle.skygrid;

import com.possibletriangle.skygrid.random.RandomManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        RandomManager.reload();
        ConfigOptions.reload();
    }

}

