package me.flame.vanish.players.chatmanager;

import me.flame.vanish.Core;
import me.flame.vanish.players.User;
import me.flame.vanish.players.chatmanager.interfaces.IChatManager;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ChatManager implements IChatManager {

    LuckPerms luckPerms = Bukkit.getServer().getServicesManager().load(LuckPerms.class);

    public static HashMap<Player, Rank> players = new HashMap<>();
    public static HashMap<String, String> chatFormats = new HashMap<>();
    public static HashMap<String, String> tabFormats = new HashMap<>();
    public static ArrayList<Rank> ranks = new ArrayList<>();

    private static ChatManager chatManager;

    public ChatManager() {
        chatManager = this;
    }

    @Override
    public void loadChatFormat() {

        Rank rank;

        for (String keys : FileManager.get("config.yml").getConfigurationSection("chat").getKeys(false)) {
            String format = FileManager.get("config.yml").getString("chat." + keys + ".format");
            chatFormats.put(keys, format);

            String name = keys;
            String prefix = FileManager.get("config.yml").getString("chat." + keys + ".prefix");
            String suffix = FileManager.get("config.yml").getString("chat." + keys + ".suffix");
            Integer priority = FileManager.get("config.yml").getInt("chat." + keys + ".priority");
            Boolean Default = FileManager.get("config.yml").getBoolean("chat." + keys + ".default");

            rank = new Rank(name, prefix, suffix, null, priority);

            if (Default) {
                rank.setDefaultRank(true);
            }

            ranks.add(rank);
            Core.getInstance().getLogger().info("I have loaded " + rank.getName() + " prefix: " + rank.getPrefix() + " prio:" + rank.getPriority() + " default: " + rank.isDefaultRank());

        }

        Core.getInstance().getLogger().info("I have loaded " + chatFormats.size() + " chat formats.");
    }

    public Rank getGroup(UUID uuid) {
        Player p = Bukkit.getServer().getPlayer(uuid);

        for (Rank ranks : ranks) {
            if (p.hasPermission("group." + ranks.getName())) {
                return ranks;
            }
        }

        return getDefaultGroup();
    }

    public Rank getDefaultGroup() {

        for (Rank ranks : ranks) {
            if (ranks.isDefaultRank()) {
                return ranks;
            }
        }

        return null;
    }

    public void setScoreboard() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Scoreboard scoreboard = p.getScoreboard();
            Bukkit.getOnlinePlayers().forEach(target -> {
                Rank rank = getGroup(target.getUniqueId());
                Integer weight = 10000 + rank.getPriority();
                String name = weight + rank.getName();
                String prefix = ChatUtils.format(rank.getPrefix());
                String suffix = ChatUtils.format(rank.getSuffix());

                target.setPlayerListHeader(ChatUtils.format(FileManager.get("config.yml").getString("config.prefix.tablist-header")));
                target.setPlayerListFooter(ChatUtils.format(FileManager.get("config.yml").getString("config.prefix.tablist-footer")));

                Team team = scoreboard.getTeam(name);
                if (team == null) {
                    team = scoreboard.registerNewTeam(name);
                }

                if (prefix == null) {
                    prefix = ChatUtils.format("&cError &7");
                }

                ChatColor color = null;
                if (prefix.endsWith(ChatUtils.format("&7"))) {
                    color = ChatColor.GRAY;
                } else if (prefix.endsWith(ChatUtils.format("&f"))) {
                    color = ChatColor.WHITE;
                } else {
                    color = ChatColor.RED;
                }

                team.setPrefix(prefix);
                team.setSuffix(suffix);
                team.setColor(color);

                team.addEntry(target.getName());


            });
        });
    }

    public void loadTabFormat() {

        for (String keys : FileManager.get("config.yml").getConfigurationSection("chat").getKeys(false)) {
            String format = FileManager.get("config.yml").getString("chat." + keys + ".prefix");
            tabFormats.put(keys, format);
        }

        Core.getInstance().getLogger().info("I have loaded " + tabFormats.size() + " tab formats.");
    }

    @Override
    public void refreshTimer() {

        new BukkitRunnable() {

            @Override
            public void run() {

                ChatManager.getInstance().setScoreboard();

                refreshTimer();
            }
        }.runTaskLater(Core.getInstance(), 1);
    }

    public static ChatManager getInstance() {
        return chatManager;
    }
}

