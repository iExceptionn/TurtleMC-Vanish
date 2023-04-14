package me.flame.vanish.donators.guis;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class UserinfoGUI {

    private final DonatorManager donatorManager = new DonatorManager();

    public Inventory openUserinfoGUI(Donator donator, Player executer) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Player p = Bukkit.getServer().getPlayer(donator.getUuid());
        Inventory inventory = Bukkit.createInventory(null, 27, ChatUtils.format("&8[&c!&8] &7Userinfo: &f" + p.getName()));

        inventory.setItem(12, new ItemBuilder(Material.NAME_TAG, 1)
                .setDisplayName("&6Displayname Information")
                .setLore(""
                        , "&7× &ePlayer info"
                        , " &f- &7Name: &f" + p.getName()
                        , ""
                        , "&7× &eDisplayname info"
                        , " &f- &7Enabled: &f" + (donatorManager.hasDisplayName(donator) ? "&atrue" : "&cfalse")
                        , " &7- &7Displayname: &f" + donator.getDisplayName()).build());


        inventory.setItem(14, new ItemBuilder(Material.ANVIL, 1)
                .setDisplayName("&6Prefix Information")
                .setLore(""
                        , "&7× &ePrefix info"
                        , " &f- &7Enabled: &f" + (donatorManager.hasPrefix(donator) ? "&atrue" : "&cfalse")
                        , " &7- &7Custom-prefix: &f" + donator.getPrefix()
                        , " &7- &7Cooldown date: &f" + sf.format(donator.getDate())).build());


        inventory.setContents(inventory.getContents());
        executer.openInventory(inventory);
        return inventory;

    }
}
