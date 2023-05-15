package me.flame.vanish.players.chatmanager.listeners;

import me.flame.vanish.Core;
import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private UserManager userManager = new UserManager();
    LuckPerms luckPerms = Bukkit.getServer().getServicesManager().load(LuckPerms.class);
    private final Core core;

    public ChatListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();


        String message = e.getMessage();
        if(p.hasPermission("chat.chatcolor")){
            message = ChatUtils.format(e.getMessage());
        }

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.sendMessage(userManager.getPlayerFormat(p.getUniqueId()).replace("{message}", message).replace("{oitc_player_level}", userManager.replace(p, "%oitc_getplayerlevel%")));
        }

        String realname = ChatUtils.format("&8[&7REAL-NAME: &e" + p.getName() + "&8]");
        Bukkit.getServer().getConsoleSender().sendMessage(realname + " " + userManager.getPlayerFormat(p.getUniqueId()).replace("{message}", message).replace("{oitc_player_level}", userManager.replace(p, "%oitc_getplayerlevel%")));
        e.setCancelled(true);
    }

}
