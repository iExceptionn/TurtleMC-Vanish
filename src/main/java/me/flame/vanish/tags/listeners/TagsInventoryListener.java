package me.flame.vanish.tags.listeners;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.commands.PrefixCommand;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.tags.Tag;
import me.flame.vanish.tags.guis.TagsGUI;
import me.flame.vanish.tags.managers.TagManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TagsInventoryListener implements Listener {

    private final TagManager tagManager = new TagManager();
    private final TagsGUI tagsGUI = new TagsGUI();
    private String prefix = FileManager.get("tags.yml").getString("config.prefix");

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        Player p = (Player) e.getWhoClicked();
        Donator donator = DonatorManager.getPlayer(p.getUniqueId());
        if (p.getOpenInventory().getTitle().contains(ChatUtils.format("&9&lTags Menu            &7&o(Page "))) {

            Integer pageNumber = 1;

            if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            } else if (e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                e.setCancelled(true);
                return;
            }
            else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {


                p.sendMessage(ChatUtils.format(FileManager.get("tags.yml").getString("config.messages.removed-tag").replaceAll("%tag_prefix%", donator.getPrefix()).replaceAll("%prefix%", prefix)));
                tagManager.setTag(donator, "none");
                tagsGUI.openTagsGUI(donator, pageNumber);
                return;
            } else {

                String Tagprefix = e.getCurrentItem().getItemMeta().getDisplayName();
                Tag tag = TagManager.getTagFromPrefix(Tagprefix);
                if (tagManager.hasTag(donator, tag)) {
                    tagManager.setTag(donator, tag.getPrefix());
                    p.sendMessage(ChatUtils.format(FileManager.get("tags.yml").getString("config.messages.selected-tag").replaceAll("%tag%", tag.getName()).replaceAll("%tag_prefix%", tag.getPrefix()).replaceAll("%prefix%", prefix)));
                    p.closeInventory();
                }

                e.setCancelled(true);
            }

            e.setCancelled(true);
        }

    }
}
