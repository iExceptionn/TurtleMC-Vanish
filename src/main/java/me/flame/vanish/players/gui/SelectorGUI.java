package me.flame.vanish.players.gui;

import me.flame.vanish.players.User;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionType;

import java.util.UUID;

public class SelectorGUI {

    public Inventory openSettingsGUI(UUID uuid) {

        Player p = Bukkit.getServer().getPlayer(uuid);
        User user = UserManager.getUser(uuid);
        Inventory inventory = Bukkit.createInventory(null, 27, ChatUtils.format("&aVanish Settings"));

        inventory.setItem(11, new ItemBuilder(Material.COMPASS, 1).setDisplayName("&aSelect Gamemode")
                .setLore(""
                        , "&7Select your gamemode here."
                        , "&7You're current gamemode: &a" + user.getGamemode()).build());

        inventory.setItem(15, new ItemBuilder(Material.POTION, 1).setPotionType(PotionType.NIGHT_VISION, true, false)
                .setItemFlag(ItemFlag.HIDE_POTION_EFFECTS).setDisplayName("&aSelect Nightvision")
                .setLore(""
                        , "&7Select if you want nightvision enabled."
                        , "&7You have it &a" + (user.getNightVision() ? "&aenabled" : "&cdisabled") + "&7.").build());

        p.openInventory(inventory);
        inventory.setContents(inventory.getContents());
        return inventory;
    }
}
