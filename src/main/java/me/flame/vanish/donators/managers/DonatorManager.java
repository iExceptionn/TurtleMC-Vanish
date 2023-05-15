package me.flame.vanish.donators.managers;

import me.flame.vanish.Core;
import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.interfaces.IDonatorManager;
import me.flame.vanish.donators.utils.AddDaysUtils;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonatorManager implements IDonatorManager {
    public static ArrayList<Donator> loadedPlayers = new ArrayList<>();
    private final LuckPerms luckPerms = Core.getApi();

    private static ArrayList<String> disabledNames = new ArrayList<>();
    private static ArrayList<String> disabledPrefix = new ArrayList<>();

    @Override
    public void registerUser(UUID uuid) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try (Connection connection = Core.hikari.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `player_data` WHERE uuid = '" + uuid + "';");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                preparedStatement.executeUpdate("INSERT INTO `player_data` (`uuid`, `name`, `display_name`, `prefix`, `prefix-cooldown`) VALUE ( '" + uuid + "' ,'" + Bukkit.getServer().getPlayer(uuid).getName() + "','" + Bukkit.getServer().getPlayer(uuid).getName() + "','none','" + dtf.format(now) + "')");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.getInstance().getLogger().info("User with name " + Bukkit.getServer().getPlayer(uuid).getName() + " UUID: " + uuid + " has been created.");
            loadUser(uuid);
        }
    }

    @Override
    public void loadUser(UUID uuid) {
        try (Connection connection = Core.hikari.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `player_data` WHERE uuid = '" + uuid + "';");
            ResultSet resultSet = preparedStatement.executeQuery();

            PreparedStatement preparedStatementGeneral = connection.prepareStatement("SELECT * FROM `general_data`;");
            ResultSet resultSetGeneral = preparedStatementGeneral.executeQuery();

            if (resultSet.next()) {
                Donator donator;
                String name = Bukkit.getServer().getPlayer(uuid).getName();
                String display_name = resultSet.getString("display_name");
                String prefix = resultSet.getString("prefix");
                String date = resultSet.getString("prefix-cooldown");

                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date2 = sf.parse(date);

                String unlockedTagsString = resultSet.getString("unlockedTags");
                ArrayList<String> unlockedTagsList = new ArrayList<>();
                String unlockedTags[] = unlockedTagsString.split(";");
                for(String string : unlockedTags){
                    unlockedTagsList.add(string);
                }

                String currentTag = resultSet.getString("currentTag");

                donator = new Donator(uuid, name, display_name, prefix, date2, unlockedTagsList, currentTag);

                loadedPlayers.add(donator);
            } else {
                registerUser(uuid);
            }

            if(resultSetGeneral.next()){
                String disablesNames = resultSetGeneral.getString("disabled-names");
                String disablesNamesList[] = disablesNames.split(";");
                for(String string : disablesNamesList){
                    disabledNames.add(string);
                }

                String disabledPrefixes = resultSetGeneral.getString("disabled-prefixes");
                String disabledPrefixesList[] = disabledPrefixes.split(";");
                for(String string : disabledPrefixesList){
                    disabledPrefix.add(string);
                }
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Core.getInstance().getLogger().info("Player with the name " + Bukkit.getPlayer(uuid).getName() + " UUID: " + uuid + " has been loaded.");
        }
    }

    @Override
    public void saveUser(UUID uuid) {
        Donator donator = DonatorManager.getPlayer(uuid);
        try (Connection connection = Core.hikari.getConnection()) {

            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `player_data` WHERE uuid = '" + uuid + "';");

            preparedStatement.executeUpdate("UPDATE `player_data` set `name` = '" + Bukkit.getServer().getPlayer(uuid).getName() + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `player_data` set `display_name` = '" + donator.getDisplayName() + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `player_data` set `prefix` = '" + donator.getPrefix() + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `player_data` set `prefix-cooldown` = '" + sf.format(donator.getDate()) + "' WHERE uuid = '" + uuid + "';");

            preparedStatement.executeUpdate("UPDATE `player_data` set `unlockedTags` = '" + donator.getUnlockedTags().stream().map(Object::toString).collect(Collectors.joining(";")) + "' WHERE uuid = '" + uuid + "';");
            preparedStatement.executeUpdate("UPDATE `player_data` set `currentTag` = '" + donator.getCurrentTag() + "' WHERE uuid = '" + uuid + "';");

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Core.getInstance().getLogger().info("Player with the name " + donator.getName() + " (" + donator.getDisplayName() + ") UUID: " + uuid + " has been saved.");
        }
    }

    @Override
    public void deleteDonator(Donator donator) {
        loadedPlayers.remove(donator);
    }

    @Override
    public boolean isDisplaynameAllowed(String name, Player p) {

        OfflinePlayer offlinePlayer2 = Bukkit.getOfflinePlayer(p.getUniqueId());

        for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
            if(offlinePlayer != offlinePlayer2) {
                if (name.toLowerCase().contains(offlinePlayer.getName().toLowerCase())) {
                    return false;
                }
            }
        }

        for(String string : disabledNames){
            if(name.toLowerCase().contains(string)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void setDisplayName(Donator donator, String name) {
        donator.setDisplayName(name);
    }

    @Override
    public boolean isPrefixAllowed(String name) {
        for(String string : disabledPrefix){
            if(name.toLowerCase().contains(string)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void setPrefix(Donator donator, String prefixRank) {

        Player p = Bukkit.getPlayer(donator.getUuid());

        User user = luckPerms.getUserManager().getUser(donator.getUuid());
        user.data().add(PrefixNode.builder(prefixRank + " &7", 62).build());
        luckPerms.getUserManager().saveUser(user);

        donator.setPrefix(prefixRank);

        // Add 30 days
        Date newDate = AddDaysUtils.addDays();
        donator.setDate(newDate);

        p.sendMessage(ChatUtils.format(Core.getDonatorPrefix() + "&7You have changed you're prefix to: " + donator.getPrefix()));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);

        saveUser(p.getUniqueId());
    }

    @Override
    public void removePrefix(Donator donator) {
        Player p = Bukkit.getPlayer(donator.getUuid());

        User user = luckPerms.getUserManager().getUser(donator.getUuid());
        user.data().remove(PrefixNode.builder(donator.getPrefix() + " &7", 62).build());
        luckPerms.getUserManager().saveUser(user);

        donator.setPrefix("none");
        donator.setDate(new Date());

        saveUser(p.getUniqueId());
    }

    @Override
    public boolean hasPrefix(Donator donator) {
        if(!donator.getPrefix().equalsIgnoreCase("none")){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasDisplayName(Donator donator) {
        if(!donator.getName().equalsIgnoreCase(donator.getDisplayName())){
            return true;
        }
        return false;
    }

    @Override
    public boolean canChangePrefix(Donator donator) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            if(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(sf.format(donator.getDate())).before(new Date())){
                return true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static Donator getPlayer(UUID uuid) {
        for (Donator donator : loadedPlayers) {
            if (donator.getUuid() == uuid) {
                return donator;
            }
        }

        return null;
    }
}
