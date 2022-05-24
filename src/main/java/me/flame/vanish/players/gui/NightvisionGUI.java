package me.flame.vanish.players.gui;

import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class NightvisionGUI {

    public Inventory nightVisionGUI(UUID uuid){

        Player p = Bukkit.getServer().getPlayer(uuid);
        Inventory inventory = Bukkit.createInventory(null, 9, ChatUtils.format("&aNightvision"));

        inventory.setItem(3, new ItemBuilder(Material.GREEN_WOOL, 1).setDisplayName("&aEnable Nightvision").build());
        inventory.setItem(5, new ItemBuilder(Material.RED_WOOL, 1).setDisplayName("&cDisable Nightvision").build());

        inventory.setContents(inventory.getContents());
        p.openInventory(inventory);

        return inventory;
    }
}
