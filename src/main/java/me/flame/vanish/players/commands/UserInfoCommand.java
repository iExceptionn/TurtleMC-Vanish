package me.flame.vanish.players.commands;

import me.flame.vanish.Core;
import me.flame.vanish.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("vanish.userinfo")) {
            switch (args.length) {
                default:
                case 0:
                    sender.sendMessage(ChatUtils.format(Core.getPrefix() + "&7Wrong usage: &a/userinfo <name>"));
                    break;
                case 1:
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(ChatUtils.format(Core.getPrefix() + "&7Players hasn't been found."));
                        break;
                    }

                    sender.sendMessage(ChatUtils.format(Core.getPrefix() + target.getName() + " &finformation:"));
                    //sender.sendMessage(ChatUtils.format(" &8- &7First joined: &f" + getDurationBreakdown(target.getFirstPlayed() - System.currentTimeMillis())));
                    //sender.sendMessage(ChatUtils.format(" &8- &7Last played: &f" + getDurationBreakdown(target.getLastPlayed() - System.currentTimeMillis())));
                    sender.sendMessage(ChatUtils.format("&8- &7Playtime: " + getPlaytime(target.getStatistic(Statistic.PLAY_ONE_MINUTE))));
                    sender.sendMessage(ChatUtils.format(""));
                    sender.sendMessage(ChatUtils.format("&7&oAdding more soon."));

                    break;
            }
        } else {
            sender.sendMessage("&7You do not have permissions to use this command.");
            return true;
        }
        return false;
    }

    public static String getPlaytime(long playtime) {

        long Totalseconds = playtime / 20;

        long hours = Totalseconds / 3600;
        long minutes = (Totalseconds % 3600) / 60;
        long seconds = Totalseconds % 60;

        return ChatUtils.format("&fHours: &a" + hours + "&7, &fMinutes: &a" + minutes + " &7& &fSeconds: &a" + seconds);
    }

}
