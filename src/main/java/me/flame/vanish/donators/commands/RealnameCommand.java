package me.flame.vanish.donators.commands;

import me.flame.vanish.Core;
import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RealnameCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7Command usage"));
            sender.sendMessage(ChatUtils.format(" &8- &f/realname <name>"));
            sender.sendMessage(ChatUtils.format(" &8- &f/realname list"));
            return true;
        }

        if(args.length == 1){
            for(Donator donator : DonatorManager.loadedPlayers){
                if(args[0].equalsIgnoreCase(donator.getDisplayName())){
                    sender.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&e" + donator.getDisplayName() + "'s &7real name is &e" + donator.getName()));
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("list")){
                sender.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7All online players nicknames."));
                for(Donator donator : DonatorManager.loadedPlayers){
                    sender.sendMessage(ChatUtils.format(" &8× &e" + donator.getName() + " &7display name is &e" + donator.getDisplayName()));
                }
                return true;
            }
            sender.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7Command usage"));
            sender.sendMessage(ChatUtils.format(" &8- &f/realname <name>"));
            sender.sendMessage(ChatUtils.format(" &8- &f/realname list"));
            return true;
        }
        return false;
    }
}
