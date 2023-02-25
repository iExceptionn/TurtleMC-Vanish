package me.flame.vanish;

import com.zaxxer.hikari.HikariDataSource;
import me.flame.vanish.players.chatmanager.ChatManager;
import me.flame.vanish.players.chatmanager.listeners.ChatListener;
import me.flame.vanish.players.commands.*;
import me.flame.vanish.players.listeners.InventoryListener;
import me.flame.vanish.players.listeners.PlayerEvents;
import me.flame.vanish.players.listeners.VanishedEvents;
import me.flame.vanish.players.managers.DatabaseManager;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin implements Listener {

    private static Core instance;
    public static HikariDataSource hikari;
    public final DatabaseManager databaseManager = new DatabaseManager();
    private final ChatManager chatManager = new ChatManager();
    private final UserManager userManager = new UserManager();

    


    public static Core getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().info("Vanish Plugin started");
        FileManager.load(this, "config.yml");
        instance = this;


        // Database
        connectMysql();
        databaseManager.createDatabase();



        // Events / Commands
        registerEvents();
        registerCommands();
        chatManager.loadChatFormat();
        chatManager.loadTabFormat();

        ChatManager.getInstance().setScoreboard();
        ChatManager.getInstance().refreshTimer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Vanish Plugin stopped");
        FileManager.configs.clear();

        ChatManager.chatFormats.clear();
        ChatManager.tabFormats.clear();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            userManager.deleteUser(online.getUniqueId());
        }


    }

    public void connectMysql() {
        hikari = new HikariDataSource();

        if (Core.getInstance().getServer().getVersion().contains("(MC: 1.15.2)")) {
            hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        } else {
            hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        }


        // Data from config
        String hostName = FileManager.get("config.yml").getString("database.host");
        String[] hostNameAndPort = hostName.split(":");
        String databaseName = FileManager.get("config.yml").getString("database.databasename");
        String databasePassword = FileManager.get("config.yml").getString("database.password");
        String databaseUser = FileManager.get("config.yml").getString("database.username");

        hikari.addDataSourceProperty("serverName", hostNameAndPort[0]);
        hikari.addDataSourceProperty("port", hostNameAndPort[1]);
        hikari.addDataSourceProperty("databaseName", databaseName);
        hikari.addDataSourceProperty("user", databaseUser);
        hikari.addDataSourceProperty("password", databasePassword);

        hikari.addDataSourceProperty("verifyServerCertificate", false);
        hikari.addDataSourceProperty("useSSL", false);

    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(this, this);

        pluginManager.registerEvents(new PlayerEvents(), this);
        pluginManager.registerEvents(new VanishedEvents(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new ChatListener(this), this);


    }

    public static String getPrefix() {
        if (FileManager.get("config.yml").getBoolean("config.prefix.enabled")) {
            return ChatUtils.format(FileManager.get("config.yml").getString("config.prefix.prefix"));
        }

        return "";
    }

    private void registerCommands() {
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("userinfo").setExecutor(new UserInfoCommand());
        getCommand("vreload").setExecutor(new ReloadCommand());
    }
}
