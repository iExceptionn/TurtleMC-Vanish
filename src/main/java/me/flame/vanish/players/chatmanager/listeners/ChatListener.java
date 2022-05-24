package me.flame.vanish.players.chatmanager.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ApiManager;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageSentEvent;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import me.flame.vanish.Core;
import me.flame.vanish.players.managers.UserManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.luckperms.api.LuckPerms;
import org.apache.commons.lang.WordUtils;
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

    @Subscribe
    public void discordReadyEvent(DiscordReadyEvent event) {

        core.getLogger().info("Chatting on Discord with " + DiscordUtil.getJda().getUsers().size() + " users!");

    }



    @SubscribeEvent
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String userGroup = luckPerms.getPlayerAdapter(Player.class).getUser(p).getPrimaryGroup();

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.sendMessage(userManager.getPlayerFormat(p.getUniqueId()).replace("{message}", e.getMessage()));
        }

        Bukkit.getServer().getConsoleSender().sendMessage(userManager.getPlayerFormat(p.getUniqueId()).replace("{message}", e.getMessage()));

        TextChannel textChannel = DiscordSRV.getPlugin().getMainTextChannel();
        DiscordUtil.sendMessage(textChannel, "**" + WordUtils.capitalizeFully(userGroup) + "** " + p.getName() + " » " + e.getMessage());
        e.setCancelled(true);
    }

}
