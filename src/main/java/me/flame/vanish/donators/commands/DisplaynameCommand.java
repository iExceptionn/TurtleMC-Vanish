package me.flame.vanish.donators.commands;

import me.flame.vanish.Core;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.donators.utils.AddDaysUtils;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DisplaynameCommand implements CommandExecutor {
    private final DonatorManager donatorManager = new DonatorManager();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if(p.hasPermission(FileManager.get("donator.yml").getString("config.permissions.set-displayname"))){
            if(args.length == 0){
                p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&fCommand usage"));
                p.sendMessage(ChatUtils.format("&8- &7/name <prefix>"));
                p.sendMessage(ChatUtils.format("&8- &7/name reset"));
                return true;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reset")){
                    donatorManager.setDisplayName(DonatorManager.getPlayer(p.getUniqueId()), p.getName());
                    p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You nickname has been reset to &e" + p.getName() + "&7!"));
                    return true;
                }
                if(donatorManager.isDisplaynameAllowed(args[0])){
                    donatorManager.setDisplayName(DonatorManager.getPlayer(p.getUniqueId()), args[0]);
                    p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You have changed your nickname to &e" + DonatorManager.getPlayer(p.getUniqueId()).getDisplayName() + "&7!"));
                    return true;
                } else {
                    p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7Displayname &c" + args[0] + " &7isn't allowed."));
                    return true;
                }
            }
            return true;
        }
        p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + FileManager.get("donator.yml").getString("config.messages.no-permissions")));
        return false;
    }
}
