package me.flame.vanish.players.commands;

import me.flame.vanish.Core;
import me.flame.vanish.utils.FileManager;
import me.flame.vanish.utils.PluginUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender.hasPermission("vanish.reload")){

            PluginUtils.restartPlugin(Core.getInstance());
            sender.sendMessage(Core.getPrefix() + "reloaded the plugin.");

            return true;
        }
        return false;
    }
}
