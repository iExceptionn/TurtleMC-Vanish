package me.flame.vanish.players.chatmanager.listeners;

import me.flame.vanish.Core;
import me.flame.vanish.players.managers.UserManager;
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

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.sendMessage(userManager.getPlayerFormat(p.getUniqueId()).replace("{message}", e.getMessage()));
        }

        Bukkit.getServer().getConsoleSender().sendMessage(userManager.getPlayerFormat(p.getUniqueId()).replace("{message}", e.getMessage()));
        e.setCancelled(true);
    }

}
