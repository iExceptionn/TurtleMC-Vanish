package me.flame.vanish.players.managers;

import me.flame.vanish.Core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager  {

    public void createDatabase(){
        try{
            Connection connection = Core.hikari.getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `staff_data` (`uuid` varchar(36) NOT NULL, `name` varchar(36) NOT NULL, `vanished` BOOLEAN NOT NULL, `gamemode` varchar(36) NOT NULL,  `nightvision` BOOLEAN NOT NULL, PRIMARY KEY (`uuid`))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `player_data` (`UUID` varchar(36) NOT NULL, `name` varchar(360) NOT NULL, `display_name` varchar(360) NOT NULL, `prefix` varchar(36) NOT NULL, `prefix-cooldown` varchar(36) NOT NULL, PRIMARY KEY (`UUID`))");

        } catch (SQLException e){
            Core.getInstance().getLogger().info("Failed to connect to a database, plugin disabled.");

            e.printStackTrace();
            Core.getInstance().getPluginLoader().disablePlugin(Core.getInstance());
        }

    }
}
