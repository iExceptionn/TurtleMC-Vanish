package me.flame.vanish.tags.guis;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.players.User;
import me.flame.vanish.tags.Tag;
import me.flame.vanish.tags.managers.TagManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class TagsGUI {

    private final TagManager tagManager = new TagManager();

    public Inventory openTagsGUI(Donator donator, Integer page) {

        Player player = Bukkit.getPlayer(donator.getUuid());
        Inventory inventory = Bukkit.createInventory(null, 54, ChatUtils.format("&9&lTags Menu            &7&o(Page " + page + ")"));

        int amount = page * 36 - 36;

        for (Tag tag : TagManager.loadedTags) {

            if (tag.isVisible()) {

                List<String> loreList = new ArrayList<>();
                for (String s : tag.getLore()) {
                    loreList.add(s);
                }


                loreList.add(tagManager.hasTag(donator, tag) ? "&fYou have &aunlocked &fthis tag." : "&fYou have &cnot unlocked &fthis tag.");

                inventory.addItem(new ItemBuilder(Material.NAME_TAG, 1).setDisplayName(tag.getPrefix()).setLore(loreList).build());
                amount++;

                if (amount == page * 36) {
                    inventory.setItem(53, new ItemBuilder(Material.ARROW, 1).setDisplayName("&fNext Page").build());
                    break;
                }

            }
        }

        inventory.setItem(36, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(37, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(38, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(39, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(40, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(41, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(42, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(43, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());
        inventory.setItem(44, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName("&f").build());

        inventory.setItem(47, new ItemBuilder(Material.PLAYER_HEAD, 1).setSkullOwner(player.getName()).setDisplayName("&9Player Info")
                .setLore(""
                        , " &7Current tag: &f" + donator.getPrefix()
                        , ""
                        , "&fClick to remove your current tag.").build());

        inventory.setItem(49, new ItemBuilder(Material.NETHER_STAR, 1).setDisplayName("&cClose Menu").build());

        if (page > 1) {
            inventory.setItem(45, new ItemBuilder(Material.ARROW, 1).setDisplayName("&fPrevious Page").build());
        }

        inventory.setContents(inventory.getContents());
        player.openInventory(inventory);

        return inventory;

    }
}
