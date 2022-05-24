package me.flame.vanish.players.listeners;

import me.flame.vanish.players.managers.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class VanishedEvents implements Listener {

    UserManager userManager = new UserManager();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (userManager.isVanished(p.getUniqueId())) {
            if (!p.hasPermission("vanish.build")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (userManager.isVanished(p.getUniqueId())) {
            if (!p.hasPermission("vanish.build")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (userManager.isVanished(p.getUniqueId())) {
            if (!p.hasPermission("vanish.drop")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void creativeInventory(InventoryCreativeEvent e){
        Player p = (Player) e.getWhoClicked();
        if(userManager.isVanished(p.getUniqueId())){
            if(!p.hasPermission("vanish.creative")){
                e.setResult(Event.Result.DENY);
            }
        }
    }

}
