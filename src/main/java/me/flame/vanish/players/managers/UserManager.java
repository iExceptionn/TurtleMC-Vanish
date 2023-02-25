package me.flame.vanish.players.managers;

import me.flame.vanish.Core;
import me.flame.vanish.players.User;
import me.flame.vanish.players.chatmanager.ChatManager;
import me.flame.vanish.players.interfaces.IUser;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UserManager implements IUser {

    private static ArrayList<User> loadedStaff = new ArrayList<>();
    public static ArrayList<String> isVanished = new ArrayList<>();
    public static HashMap<UUID, GameMode> playerGamemode = new HashMap<>();

    LuckPerms luckPerms = Bukkit.getServer().getServicesManager().load(LuckPerms.class);

    @Override
    public void createStaff(UUID uuid) {
        try (Connection connection = Core.hikari.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `staff_data` WHERE uuid = '" + uuid + "';");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                preparedStatement.executeUpdate("INSERT INTO `staff_data` (`uuid`, `name`, `vanished`, `gamemode`, `nightvision`) VALUE ( '" + uuid + "' ,'" + Bukkit.getServer().getPlayer(uuid).getName() + "',0,'spectator',0)");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.getInstance().getLogger().info("Staff with the name " + Bukkit.getServer().getPlayer(uuid).getName() + " UUID: " + uuid + " has been created.");
            loadStaff(uuid);
        }
    }

    @Override
    public void loadStaff(UUID uuid) {
        try (Connection connection = Core.hikari.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `staff_data` WHERE uuid = '" + uuid + "';");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user;
                String name = Bukkit.getServer().getPlayer(uuid).getName();
                Boolean vanished = resultSet.getBoolean("vanished");
                String gamemode = resultSet.getString("gamemode");
                Boolean nightVision = resultSet.getBoolean("nightvision");

                user = new User(uuid, name, vanished, gamemode, nightVision);

                loadedStaff.add(user);
            } else {
                createStaff(uuid);
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Core.getInstance().getLogger().info("Staff with the name " + Bukkit.getPlayer(uuid).getName() + " UUID: " + uuid + " has been loaded.");
        }
    }

    @Override
    public void saveStaff(UUID uuid) {
        User user = getUser(uuid);
        try (Connection connection = Core.hikari.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `staff_data` WHERE uuid = '" + uuid + "';");

            Integer vanished = 0;
            if (user.getVanished()) {
                vanished = 1;
            }

            Integer nightVision = 0;
            if (user.getNightVision()) {
                nightVision = 1;
            }

            preparedStatement.executeUpdate("UPDATE `staff_data` set `name` = '" + Bukkit.getServer().getPlayer(uuid).getName() + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `staff_data` set `vanished` = '" + vanished + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `staff_data` set `gamemode` = '" + user.getGamemode() + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `staff_data` set `nightvision` = '" + nightVision + "' WHERE uuid = '" + uuid + "';");

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            Core.getInstance().getLogger().info("Staff with the name " + Bukkit.getPlayer(uuid).getName() + " UUID: " + uuid + " has been saved.");
        }
    }

    @Override
    public void vanishPlayer(UUID uuid, Boolean message) {
        Player p = Bukkit.getServer().getPlayer(uuid);
        User user = getUser(uuid);
        if (!isVanished(uuid)) {
            p.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.vanish")));
            user.setVanished(true);
            isVanished.add(p.getName());
            playerGamemode.put(p.getUniqueId(), p.getGameMode());
            p.setGameMode(getGamemode(p.getUniqueId()));

            if (user.getNightVision()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300000, 0));
            }
            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                if (message) {
                    if (FileManager.get("config.yml").getBoolean("config.quit-message.enabled")) {
                        all.sendMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.quit-message.message")).replace("{name}", p.getName()).replace("{name}", p.getName()).replace("{rank}", getPrefix(p.getUniqueId())));
                    }
                }
                if (all.hasPermission("vanish.see")) {
                    if (all != p) {
                        all.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.vanish-all").replace("{name}", p.getName())));
                    }
                } else {
                    all.hidePlayer(Core.getInstance(), p);
                }

            }
            return;
        } else {
            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                if (FileManager.get("config.yml").getBoolean("config.quit-message.enabled")) {
                    all.sendMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.quit-message.message")).replace("{name}", p.getName()).replace("{name}", p.getName()).replace("{rank}", getPrefix(p.getUniqueId())));
                }
                if (!all.hasPermission("vanish.see")) {
                    all.hidePlayer(Core.getInstance(), p);
                }
            }
            p.sendMessage(ChatUtils.format("&a[VANISH] &7You have &avanished&7."));
            isVanished.add(p.getName());
            playerGamemode.put(p.getUniqueId(), p.getGameMode());
            user.setVanished(true);
            p.setGameMode(GameMode.CREATIVE);
            return;
        }
    }

    @Override
    public void unVanishPlayer(UUID uuid, Boolean message) {
        Player p = Bukkit.getServer().getPlayer(uuid);
        User user = getUser(uuid);
        if (isVanished(uuid)) {
            p.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.unvanish")));
            user.setVanished(false);
            isVanished.remove(p.getName());
            p.setGameMode(playerGamemode.get(p.getUniqueId()));
            playerGamemode.remove(p.getUniqueId());
            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                p.removePotionEffect(potionEffect.getType());
            }
            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                all.showPlayer(Core.getInstance(), p);
                if (FileManager.get("config.yml").getBoolean("config.join-message.enabled")) {
                    all.sendMessage(ChatUtils.format(FileManager.get("config.yml").getString("config.join-message.message")).replace("{name}", p.getName()).replace("{name}", p.getName()).replace("{rank}", getPrefix(p.getUniqueId())));
                }
                if (all.hasPermission("vanish.see")) {
                    if (message) {
                        if (all != p) {
                            all.sendMessage(ChatUtils.format(Core.getPrefix() + FileManager.get("config.yml").getString("config.messages.unvanish-all").replace("{name}", p.getName())));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Boolean isVanished(UUID uuid) {
        if (isVanished.contains(Bukkit.getServer().getPlayer(uuid).getName())) {
            return true;
        }
        return false;
    }

    public static User getUser(UUID uuid) {
        for (User user : loadedStaff) {
            if (user.getUuid() == uuid) {
                return user;
            }
        }

        return null;
    }

    public void deleteUser(UUID uuid) {
        User user = getUser(uuid);
        if (user != null) {
            loadedStaff.remove(user);
        }
    }

    public String getPrefix(UUID uuid) {

        CachedMetaData cachedMetaData = luckPerms.getPlayerAdapter(Player.class).getMetaData(Bukkit.getServer().getPlayer(uuid));

        return ChatUtils.format(cachedMetaData.getPrefix());
    }

    public String getSuffix(UUID uuid) {

        CachedMetaData cachedMetaData = luckPerms.getPlayerAdapter(Player.class).getMetaData(Bukkit.getServer().getPlayer(uuid));

        return ChatUtils.format(cachedMetaData.getSuffix());
    }
    public String getPlayerFormat(UUID uuid) {
        Player p = Bukkit.getServer().getPlayer(uuid);
        String userGroup = luckPerms.getPlayerAdapter(Player.class).getUser(p).getPrimaryGroup();
        for (String formats : ChatManager.chatFormats.keySet()) {
            if (formats.equalsIgnoreCase(userGroup)) {
                return ChatUtils.format(ChatManager.chatFormats.get(formats)).replace("{name}", p.getName()).replace("{prefix}", getPrefix(p.getUniqueId())).replace("{suffix}", getSuffix(p.getUniqueId()));
            }
        }

        return ChatUtils.format(ChatManager.chatFormats.get("default").replace("{name}", p.getName()).replace("{prefix}", getPrefix(p.getUniqueId())).replace("{suffix}", getSuffix(p.getUniqueId())));
    }

    public String getPlayerTabFormat(UUID uuid) {
        Player p = Bukkit.getServer().getPlayer(uuid);
        String userGroup = luckPerms.getPlayerAdapter(Player.class).getUser(p).getPrimaryGroup();
        for (String tabFormats : ChatManager.tabFormats.keySet()) {
            if (tabFormats.equalsIgnoreCase(userGroup)) {
                return ChatUtils.format(ChatManager.tabFormats.get(tabFormats)).replace("{name}", p.getName()).replace("{prefix}", getPrefix(p.getUniqueId()));
            }
        }

        return ChatUtils.format(ChatManager.tabFormats.get("default").replace("{name}", p.getName()).replace("{prefix}", getPrefix(p.getUniqueId())));
    }

    public GameMode getGamemode(UUID uuid) {
        Player p = Bukkit.getServer().getPlayer(uuid);
        User user = getUser(p.getUniqueId());
        switch (user.getGamemode().toLowerCase()) {
            default:
            case "spectator":
                return GameMode.SPECTATOR;
            case "creative":
                return GameMode.CREATIVE;
        }
    }
}
