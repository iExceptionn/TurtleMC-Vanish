package me.flame.vanish.players.commands;

import me.flame.vanish.Core;
import me.flame.vanish.players.gui.SelectorGUI;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class VanishCommand implements CommandExecutor {

    UserManager userManager = new UserManager();
    SelectorGUI selectorGUI = new SelectorGUI();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            if (p.hasPermission("vanish.vanish")) {
                if (userManager.isVanished(p.getUniqueId())) {
                    userManager.unVanishPlayer(p.getUniqueId(), true);
                    return true;
                }
                userManager.vanishPlayer(p.getUniqueId(), true);
                return true;
            } else {
                p.sendMessage(ChatUtils.format("&fYou can try and apply for staff in the Discord."));
                p.sendMessage(ChatUtils.format("&8- &ahttps://discord.gg/kWeyhwbVNn"));
            }
        }
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                default:
                case "list":
                    if (p.hasPermission("vanish.list")) {
                        if (UserManager.isVanished.size() == 0) {
                            p.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.no-vanished-staff")));
                        } else {

                            p.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.vanish-list")).replace("{staff}", userManager.isVanished.stream().map(Object::toString).collect(Collectors.joining(ChatUtils.format("&7, &f")))));
                        }
                        break;
                    }
                case "settings":
                    if(p.hasPermission("vanish.settings")){
                        selectorGUI.openSettingsGUI(p.getUniqueId());
                        break;
                    }
                    break;
            }
        }
        return true;
    }
}
