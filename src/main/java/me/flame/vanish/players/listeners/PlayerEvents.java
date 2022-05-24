package me.flame.vanish.players.listeners;

import me.flame.vanish.Core;
import me.flame.vanish.players.User;
import me.flame.vanish.players.chatmanager.ChatManager;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerEvents implements Listener {

    UserManager userManager = new UserManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        ChatManager.getInstance().setScoreboard();
        //ChatManager.getInstance().setRank(p);
        //ChatManager.getInstance().refreshAll();
        //p.setPlayerListName(userManager.getPlayerTabFormat(p.getUniqueId()));
        if (p.hasPermission("vanish.vanish")) {
            userManager.loadStaff(p.getUniqueId());

            User user = UserManager.getUser(p.getUniqueId());
            if (user.getVanished()) {
                userManager.vanishPlayer(p.getUniqueId(), false);
            }
        }

        User user = UserManager.getUser(p.getUniqueId());
        if (FileManager.get("config.yml").getBoolean("config.join-message.enabled")) {
            if (user != null) {
                if (!user.getVanished()) {
                    e.setJoinMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.join-message.message")).replace("{name}", p.getName()).replace("{rank}", userManager.getPrefix(p.getUniqueId())));
                } else {
                    e.setJoinMessage(null);
                }
            } else {
                e.setJoinMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.join-message.message")).replace("{name}", p.getName()).replace("{name}", p.getName()).replace("{rank}", userManager.getPrefix(p.getUniqueId())));
            }
        }


        for (Player online : Bukkit.getServer().getOnlinePlayers()) {

            if (userManager.isVanished(online.getUniqueId())) {
                if (!p.hasPermission("vanish.see")) {
                    p.hidePlayer(Core.getInstance(), online);
                } else {
                    if (online.hasPermission("vanish.see")) {
                        if (online != p) {
                            online.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.vanish-join").replace("{name}", p.getName())));
                        }
                    }
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                ChatManager.getInstance().setScoreboard();
            }
        }, 20L);


    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        User user = UserManager.getUser(p.getUniqueId());
        if (FileManager.get("config.yml").getBoolean("config.quit-message.enabled")) {
            if (user != null) {
                if (!user.getVanished()) {
                    e.setQuitMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.quit-message.message")).replace("{name}", p.getName()).replace("{name}", p.getName()).replace("{rank}", userManager.getPrefix(p.getUniqueId())));
                } else {
                    e.setQuitMessage(null);
                }
            } else {
                e.setQuitMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.quit-message.message")).replace("{name}", p.getName()).replace("{name}", p.getName()).replace("{rank}", userManager.getPrefix(p.getUniqueId())));
            }
        }

        if (p.hasPermission("vanish.vanish") && UserManager.getUser(p.getUniqueId()) != null) {
            userManager.saveStaff(p.getUniqueId());

            if (userManager.isVanished(p.getUniqueId())) {
                p.setGameMode(UserManager.playerGamemode.get(p.getUniqueId()));
                UserManager.playerGamemode.remove(p.getUniqueId());
                UserManager.isVanished.remove(p.getName());

                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (online.hasPermission("vanish.see")) {
                        online.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.vanish-quit").replace("{name}", p.getName())));
                    }
                }
            }

            userManager.deleteUser(p.getUniqueId());
        }

        if(ChatManager.players.containsKey(p)){
            ChatManager.players.get(p).removePlayer(p);
            ChatManager.players.remove(p);
        }
    }
}
