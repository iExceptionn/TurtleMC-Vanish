package me.flame.vanish.donators.interfaces;

import me.flame.vanish.donators.Donator;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IDonatorManager {

    void registerUser(UUID uuid);
    void loadUser(UUID uuid);
    void saveUser(UUID uuid);
    void deleteDonator(Donator donator);
    boolean isDisplaynameAllowed(String name, Player p);
    void setDisplayName(Donator donator, String name);
    boolean isPrefixAllowed(String name);
    void setPrefix(Donator donator, String prefix);
    void removePrefix(Donator donator);
    boolean hasPrefix(Donator donator);
    boolean hasDisplayName(Donator donator);
    boolean canChangePrefix(Donator donator);
}
