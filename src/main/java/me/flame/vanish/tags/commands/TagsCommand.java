package me.flame.vanish.tags.commands;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.players.User;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.tags.Tag;
import me.flame.vanish.tags.guis.TagsGUI;
import me.flame.vanish.tags.managers.TagManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagsCommand implements CommandExecutor {
    private final TagsGUI tagsGUI = new TagsGUI();
    private final TagManager tagManager = new TagManager();
    private String prefix = FileManager.get("tags.yml").getString("config.prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) return true;

            Player player = (Player) sender;
            Donator donator = DonatorManager.getPlayer(player.getUniqueId());

            tagsGUI.openTagsGUI(donator, 1);

            return true;
        }
        if (sender.hasPermission("tags.admin")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatUtils.format("         &6&lCamel Tags "));
                    sender.sendMessage(ChatUtils.format(""));
                    sender.sendMessage(ChatUtils.format(" &8× &f/tags help &8- &7Shows this menu."));
                    sender.sendMessage(ChatUtils.format(" &8× &f/tags give <player> <tag> &8- &7Give the player a tag"));
                    sender.sendMessage(ChatUtils.format(" &8× &f/tags remove <player> <tag> &8- &7Remove the tag from a player"));
                    sender.sendMessage(ChatUtils.format(" &8× &f/tags set <player> <tag>/reset &8- &7Force a player to use a tag."));
                    sender.sendMessage(ChatUtils.format(""));
                    return true;
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatUtils.format("&6Command usage"));
                        sender.sendMessage(ChatUtils.format(" &8× &f/tags give <player> <tag> &8- &7Give the player a tag"));
                        return true;
                    } else if (args.length == 2) {
                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7Player is offline or doesn't exists."));
                            return true;
                        }
                        sender.sendMessage(ChatUtils.format("&6Command usage"));
                        sender.sendMessage(ChatUtils.format(" &8× &f/tags give <player> <tag> &8- &7Give the player a tag"));
                        return true;
                    } else if (args.length == 3) {

                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        Donator donator = DonatorManager.getPlayer(player.getUniqueId());

                        Tag tag = TagManager.getTag(args[2]);
                        if (tag == null) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7The tag you choose doesn't exists."));
                            return true;
                        }

                        if (tagManager.hasTag(donator, tag)) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7Player already has the tag &f" + tag.getName() + "&7."));
                            return true;
                        }

                        List<String> unlockedTags = donator.getUnlockedTags();
                        unlockedTags.add(tag.getName());
                        donator.setUnlockedTags(unlockedTags);

                        sender.sendMessage(ChatUtils.format(prefix + "&7You gave the &f" + tag.getName() + " tag &7to &f" + player.getName() + "&7."));
                        player.sendMessage(ChatUtils.format(prefix + "&7You have unlocked the &f" + tag.getName() + " &ftag."));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatUtils.format("&6Command usage"));
                        sender.sendMessage(ChatUtils.format(" &8× &f/tags remove <player> <tag> &8- &7Remove the tag from a player"));
                        return true;
                    } else if (args.length == 2) {
                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7Player is offline or doesn't exists."));
                            return true;
                        }
                        sender.sendMessage(ChatUtils.format("&6Command usage"));
                        sender.sendMessage(ChatUtils.format(" &8× &f/tags remove <player> <tag> &8- &7Remove the tag from a player"));
                        return true;
                    } else if (args.length == 3) {

                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        Donator donator = DonatorManager.getPlayer(player.getUniqueId());

                        Tag tag = TagManager.getTag(args[2]);
                        if (tag == null) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7The tag you choose doesn't exists."));
                            return true;
                        }

                        if (!tagManager.hasTag(donator, tag)) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7Player doesn't have the tag &f" + tag.getName() + "&7."));
                            return true;
                        }

                        List<String> unlockedTags = donator.getUnlockedTags();
                        unlockedTags.remove(tag.getName());
                        if (unlockedTags.size() == 0) {
                            unlockedTags.add("none");
                        }
                        donator.setUnlockedTags(unlockedTags);

                        sender.sendMessage(ChatUtils.format(prefix + " &7You removed the &f" + tag.getName() + " tag &7from &f" + player.getName() + "&7."));
                        player.sendMessage(ChatUtils.format(prefix + " &7You have no longer access to the &f" + tag.getName() + " &ftag."));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatUtils.format("&6Command usage"));
                        sender.sendMessage(ChatUtils.format(" &8× &f/tags set <player> <tag>/reset &8- &7Force a player to use a tag."));
                        return true;
                    } else if (args.length == 2) {
                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7Player is offline or doesn't exists."));
                            return true;
                        }
                        sender.sendMessage(ChatUtils.format("&6Command usage"));
                        sender.sendMessage(ChatUtils.format(" &8× &f/tags set <player> <tag>/reset &8- &7Force a player to use a tag."));
                        return true;
                    } else if (args.length == 3) {

                        Player player = Bukkit.getServer().getPlayer(args[1]);
                        Donator donator = DonatorManager.getPlayer(player.getUniqueId());
                        if (args[2].equalsIgnoreCase("reset")) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7You have resetted the tag of &f" + player.getName() + "&7."));
                            player.sendMessage(ChatUtils.format(prefix + " &7You're tag has been removed."));

                            tagManager.setTag(donator, "none");
                            return true;
                        }
                        Tag tag = TagManager.getTag(args[2]);
                        if (tag == null) {
                            sender.sendMessage(ChatUtils.format(prefix + " &7The tag you choose doesn't exists."));
                            return true;
                        }

                        tagManager.setTag(donator, tag.getPrefix());

                        sender.sendMessage(ChatUtils.format(prefix + " &7You have changed the prefix of &f" + player.getName() + " to &f" + tag.getPrefix() + "&7."));
                        player.sendMessage(ChatUtils.format(prefix + " &7You're prefix has ben changed to &f" + tag.getPrefix() + "&7."));
                        return true;
                    }
                }
            }
        } else {
            sender.sendMessage(ChatUtils.format(prefix + " &7You do not have the permissions to use this command."));
        }
        return false;
    }
}
