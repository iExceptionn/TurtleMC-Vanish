package me.flame.vanish.donators.commands;

import me.flame.vanish.Core;
import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.guis.PrefixGUI;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrefixCommand implements CommandExecutor {

    public static HashMap<Donator, String> playerPrefix = new HashMap<>();
    private final DonatorManager donatorManager = new DonatorManager();
    private final PrefixGUI prefixGUI = new PrefixGUI();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (p.hasPermission(FileManager.get("donator.yml").getString("config.permissions.set-prefix"))) {
            Donator donator = DonatorManager.getPlayer(p.getUniqueId());
            if(args.length == 0){
                sender.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7Command usage"));
                sender.sendMessage(ChatUtils.format(" &8- &f/prefix <prefix>"));
                return true;
            }
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("confirm")){
                    if(playerPrefix.containsKey(donator)){
                        prefixGUI.ConfirmGUI(donator, playerPrefix.get(donator));
                        return true;
                    }
                    p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You need to choose a prefix first."));
                    return true;
                }
                String RankPrefix = "";
                for(String part : args){
                    if(RankPrefix != "") RankPrefix += " ";
                    RankPrefix += part;
                }

                if(RankPrefix.length() > 48){
                    p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You have used to many characters for the prefix. Limit is &c48 characters&7."));
                    return true;
                }

                if(!donatorManager.isPrefixAllowed(RankPrefix)){
                    p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7Prefix &c" + args[0] + " &7isn't allowed."));
                    return true;
                }

                List<String> message = FileManager.get("donator.yml").getStringList("config.messages.change-prefix-info");
                String finalRankPrefix = RankPrefix;
                message.forEach((String msg) -> p.sendMessage(ChatUtils.format(msg.replaceAll("%prefix%", finalRankPrefix).replace("%displayname%", donator.getDisplayName()))));
                playerPrefix.put(donator, RankPrefix);

                return true;
            }


            return true;
        }
        p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + FileManager.get("donator.yml").getString("config.messages.no-permissions")));
        return false;
    }
}
