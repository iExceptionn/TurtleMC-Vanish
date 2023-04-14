package me.flame.vanish.donators.guis;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.donators.utils.AddDaysUtils;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

public class PrefixGUI {

    private final DonatorManager donatorManager = new DonatorManager();

    public Inventory ConfirmGUI(Donator donator, String prefix) {
        Player p = Bukkit.getPlayer(donator.getUuid());
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Inventory inventory = Bukkit.createInventory(null, 27, ChatUtils.format("&8[&c!&8] &7Prefix Changer"));

        if (donatorManager.canChangePrefix(donator)) {
            inventory.setItem(11, new ItemBuilder(Material.GREEN_CONCRETE, 1).setDisplayName("&a&lCONFIRM PREFIX").build());
        } else {
            inventory.setItem(11, new ItemBuilder(Material.BARRIER, 1).setDisplayName("&c&lUnable to change prefix")
                    .setLore(""
                            , "&7 You can change you're prefix on: &c" + sf.format(donator.getDate()) + "&7!").build());
        }

        inventory.setItem(13, new ItemBuilder(Material.NAME_TAG, 1).setDisplayName("&6Prefix Info")
                .setLore(""
                        , " &7" + prefix + " &f" + donator.getDisplayName() + " &8» &7This is your chat layout."
                        , " &7" + prefix + " &8| &f" + donator.getName() + " &7&o(Tablist Layout)"
                        , ""
                        , "&7Next change date: &f" + sf.format(AddDaysUtils.addDays()) + "&7!").build());

        inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE, 1).setDisplayName("&c&lDENY PREFIX").build());

        inventory.setContents(inventory.getContents());
        p.openInventory(inventory);
        return inventory;
    }
}
