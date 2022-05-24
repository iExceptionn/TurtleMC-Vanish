package me.flame.vanish.players.listeners;

import me.flame.vanish.Core;
import me.flame.vanish.players.User;
import me.flame.vanish.players.gui.GamemodeGUI;
import me.flame.vanish.players.gui.NightvisionGUI;
import me.flame.vanish.players.gui.SelectorGUI;
import me.flame.vanish.players.managers.UserManager;
import me.flame.vanish.utils.ChatUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener implements Listener {

    GamemodeGUI gamemodeGUI = new GamemodeGUI();
    SelectorGUI selectorGUI = new SelectorGUI();
    NightvisionGUI nightvisionGUI = new NightvisionGUI();

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        Player p = (Player) e.getWhoClicked();
        User user = UserManager.getUser(p.getUniqueId());

        if (p.getOpenInventory().getTitle().contains(ChatUtils.format("&aVanish Settings"))) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.COMPASS) {
                gamemodeGUI.openGamemodeGUI(p.getUniqueId());
                return;
            }
            if(e.getCurrentItem().getType() == Material.POTION){
                nightvisionGUI.nightVisionGUI(p.getUniqueId());
                return;
            }
        }

        if (p.getOpenInventory().getTitle().contains(ChatUtils.format("&aGamemode Selector"))) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.GRASS_BLOCK) {
                if (p.hasPermission("vanish.creative")) {
                    user.setGamemode("Creative");
                    if (user.getVanished()) {
                        p.setGameMode(GameMode.CREATIVE);
                    }
                    p.sendMessage(ChatUtils.format(Core.getPrefix() + "&7Changed your gamemode to &aCreative&7."));
                    selectorGUI.openSettingsGUI(p.getUniqueId());
                    return;
                }
            }
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                if (p.hasPermission("vanish.spectator")) {
                    user.setGamemode("Spectator");
                    if (user.getVanished()) {
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                    p.sendMessage(ChatUtils.format(Core.getPrefix() + "&7Changed your gamemode to &aSpectator&7."));
                    selectorGUI.openSettingsGUI(p.getUniqueId());
                    return;
                }
            }

        }
        if(p.getOpenInventory().getTitle().contains(ChatUtils.format("&aNightvision"))){
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.GREEN_WOOL){
                user.setNightVision(true);
                p.sendMessage(ChatUtils.format(Core.getPrefix() + "&7You &aenabled &7nightvision."));
                if(user.getVanished()){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 30000, 0));
                }
                selectorGUI.openSettingsGUI(p.getUniqueId());
                return;
            }
            if(e.getCurrentItem().getType() == Material.RED_WOOL){
                user.setNightVision(false);
                p.sendMessage(ChatUtils.format(Core.getPrefix() + "&7You &cdisabled &7nightvision."));
                if(user.getVanished()){
                    for(PotionEffect potionEffect : p.getActivePotionEffects()){
                        p.removePotionEffect(potionEffect.getType());
                    }
                }
                selectorGUI.openSettingsGUI(p.getUniqueId());
                return;
            }
        }
    }
}
