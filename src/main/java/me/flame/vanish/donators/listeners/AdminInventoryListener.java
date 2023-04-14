package me.flame.vanish.donators.listeners;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.commands.PrefixCommand;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.players.User;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.SimpleDateFormat;

public class AdminInventoryListener implements Listener {

    private final String prefix = FileManager.get("donator.yml").getString("config.prefix");
    private final DonatorManager donatorManager = new DonatorManager();

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        Player p = (Player) e.getWhoClicked();

        if (p.getOpenInventory().getTitle().contains(ChatUtils.format("&8[&c!&8] &7Userinfo: &f"))) {
            e.setCancelled(true);
        }

        if (p.getOpenInventory().getTitle().contains(ChatUtils.format("&8[&c!&8] &7Prefix Changer"))) {
            Donator donator = DonatorManager.getPlayer(p.getUniqueId());
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                if(!donatorManager.canChangePrefix(donator)){
                    p.sendMessage(ChatUtils.format(prefix + "&7You can change your prefix on: &c" + sf.format(donator.getDate()) + "&7!"));
                    return;
                }
                donatorManager.setPrefix(donator, PrefixCommand.playerPrefix.get(donator));
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().getType() == Material.RED_CONCRETE) {
                p.sendMessage(ChatUtils.format(prefix + "&7You have left the prefix editor."));
                PrefixCommand.playerPrefix.remove(donator);
                p.closeInventory();
            }
        }
    }
}
