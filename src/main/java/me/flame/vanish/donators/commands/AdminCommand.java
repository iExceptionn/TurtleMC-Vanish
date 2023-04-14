package me.flame.vanish.donators.commands;

import me.flame.vanish.Core;
import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.guis.UserinfoGUI;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.players.User;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private final UserinfoGUI userinfoGUI = new UserinfoGUI();
    private final DonatorManager donatorManager = new DonatorManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (p.hasPermission("donator.admin")) {
            if (args.length == 0) {
                p.sendMessage(ChatUtils.format(" &8[&c&l!&8] &7- &cDonator Admin &7- &8[&c&l!&8]"));
                p.sendMessage("");
                p.sendMessage(ChatUtils.format("&8- &7/da &fuserinfo &c<name>"));
                p.sendMessage(ChatUtils.format("&8- &7/da &fresetprefix &c<name>"));
                p.sendMessage(ChatUtils.format("&8- &7/da &fsetdisplayname &c<name>"));
                p.sendMessage("");
                p.sendMessage(ChatUtils.format(" &8[&c&l!&8] &7- &cDonator Admin &7- &8[&c&l!&8]"));
                return true;
            }
            if (args.length >= 1) {
                switch (args[0]) {
                    default:
                        p.sendMessage(ChatUtils.format(" &8[&c&l!&8] &7- &cDonator Admin &7- &8[&c&l!&8]"));
                        p.sendMessage("");
                        p.sendMessage(ChatUtils.format("&8- &7/da &fuserinfo &c<name>"));
                        p.sendMessage(ChatUtils.format("&8- &7/da &fresetprefix &c<name>"));
                        p.sendMessage(ChatUtils.format("&8- &7/da &fsetdisplayname &c<name> <displayname>/reset"));
                        p.sendMessage("");
                        p.sendMessage(ChatUtils.format(" &8[&c&l!&8] &7- &cDonator Admin &7- &8[&c&l!&8]"));
                        break;
                    case "userinfo":
                        if (args.length == 1) {
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7Wrong usage: &7/da &fuserinfo &c<name>"));
                            break;
                        }
                        if (args.length == 2) {
                            Player player = Bukkit.getServer().getPlayer(args[1]);
                            for (Donator donator : DonatorManager.loadedPlayers) {
                                if (args[1].equalsIgnoreCase(donator.getDisplayName())) {
                                    player = Bukkit.getServer().getPlayer(donator.getName());
                                    break;
                                }
                            }
                            if (player == null) {
                                p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7The player with name: &c" + args[1] + " &7is offline or not found."));
                                break;
                            }
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7Opening info about player &c" + player.getName() + "&7!"));
                            userinfoGUI.openUserinfoGUI(DonatorManager.getPlayer(player.getUniqueId()), p);
                            break;
                        }
                        break;
                    case "setdisplayname":
                        if (args.length == 1) {
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7Wrong usage: &7/da &fsetdisplayname <name> &c<displayname>"));
                            break;
                        }
                        if (args.length == 2) {
                            Player player = Bukkit.getServer().getPlayer(args[1]);
                            if (player == null) {
                                p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7The player with name: &c" + args[1] + " &7is offline or not found."));
                                break;
                            }
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7Wrong usage: &7/da &fsetdisplayname <name> &c<displayname>"));
                            break;
                        }
                        if (args.length == 3) {
                            Player player = Bukkit.getServer().getPlayer(args[1]);
                            if (!donatorManager.isDisplaynameAllowed(args[2])) {
                                p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7Displayname &c" + args[2] + " &7isn't allowed."));
                                break;
                            }
                            if (args[2].equalsIgnoreCase("reset")) {
                                DonatorManager.getPlayer(player.getUniqueId()).setDisplayName(player.getName());
                                p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7You have changed &c" + player.getName() + " &7displayname to &c" + player.getName()));
                                player.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You're displayname has been changed to &c" + player.getName() + " &7by a staffmember."));
                                break;
                            }
                            DonatorManager.getPlayer(player.getUniqueId()).setDisplayName(args[2]);
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7You have changed &c" + player.getName() + " &7displayname to &c" + DonatorManager.getPlayer(player.getUniqueId()).getDisplayName()));
                            player.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You're displayname has been changed to &c" + DonatorManager.getPlayer(player.getUniqueId()).getDisplayName() + " &7by a staffmember."));
                            break;
                        }
                        break;
                    case "resetprefix":
                        if (args.length == 1) {
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7Wrong usage: &7/da &fresetprefix <name>"));
                            break;
                        }
                        if (args.length == 2) {
                            Player player = Bukkit.getServer().getPlayer(args[1]);
                            if (player == null) {
                                p.sendMessage(ChatUtils.format("&8[&c&l!&8] &7The player with name: &c" + args[1] + " &7is offline or not found."));
                                break;
                            }
                            donatorManager.removePrefix(DonatorManager.getPlayer(player.getUniqueId()));
                            p.sendMessage(ChatUtils.format("&8[&c&l!&8]" + " &7You have removed &c" + player.getName() + "'s &7prefix."));
                            break;

                        }

                        break;
                }
            }
            return true;
        }
        p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + FileManager.get("donator.yml").getString("config.messages.no-permissions")));
        return false;
    }
}
