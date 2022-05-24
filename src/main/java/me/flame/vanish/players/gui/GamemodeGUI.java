package me.flame.vanish.players.gui;

import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class GamemodeGUI {

    public Inventory openGamemodeGUI(UUID uuid){

        Player p = Bukkit.getServer().getPlayer(uuid);
        Inventory inventory = Bukkit.createInventory(null, 9, ChatUtils.format("&aGamemode Selector"));

        inventory.setItem(3, new ItemBuilder(Material.GRASS_BLOCK, 1).setDisplayName("&aCreative").build());
        inventory.setItem(5, new ItemBuilder(Material.PLAYER_HEAD, 1).setSkullOwner(p.getName()).setDisplayName("&aSpectator").build());

        inventory.setContents(inventory.getContents());
        p.openInventory(inventory);
        return inventory;
    }
}
