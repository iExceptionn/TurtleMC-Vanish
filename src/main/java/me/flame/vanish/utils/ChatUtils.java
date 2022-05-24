package me.flame.vanish.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String format(String input){

        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
