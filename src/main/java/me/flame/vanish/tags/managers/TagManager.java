package me.flame.vanish.tags.managers;

import me.flame.vanish.Core;
import me.flame.vanish.donators.Donator;
import me.flame.vanish.donators.managers.DonatorManager;
import me.flame.vanish.tags.Tag;
import me.flame.vanish.tags.interfaces.iTagManager;
import me.flame.vanish.utils.ChatUtils;
import me.flame.vanish.utils.FileManager;

import java.util.ArrayList;
import java.util.List;

public class TagManager implements iTagManager {

    private final DonatorManager donatorManager = new DonatorManager();
    public static ArrayList<Tag> loadedTags = new ArrayList();
    @Override
    public void loadTags() {

        for(String key : FileManager.get("tags.yml").getConfigurationSection("tags").getKeys(false)){

            String name = key;
            String prefix = FileManager.get("tags.yml").getString("tags." + key + ".prefix");
            List<String> lore = FileManager.get("tags.yml").getStringList("tags." + key + ".lore");
            boolean visible = FileManager.get("tags.yml").getBoolean("tags." + key + ".visible");
            boolean enabled = FileManager.get("tags.yml").getBoolean("tags." + key + ".enabled");

            Tag tag = new Tag(name, prefix, lore, visible , enabled);

            loadedTags.add(tag);
        }

        Core.getInstance().getLogger().info("I have loaded " + loadedTags.size() + " tags.");
    }

    @Override
    public void createTag(String name, String prefix, String[] lore, boolean visble, boolean enabled) {

    }

    @Override
    public boolean tagIdUsed(String name) {
        return false;
    }

    @Override
    public void deleteTag(Tag tag) {

    }

    @Override
    public void setTag(Donator donator, String prefix) {
        donator.setCurrentTag(prefix);

        donatorManager.saveUser(donator.getUuid());
    }

    @Override
    public boolean hasTag(Donator donator, Tag tag) {

        if(donator.getUnlockedTags().contains(tag.getName())){
            return true;
        }

        return false;
    }

    @Override
    public void addTag(Donator donator) {

    }

    @Override
    public void removeTag(Donator donator) {

    }


    public static Tag getTag(String name){

        for(Tag tag : loadedTags){
            if(tag.getName().equalsIgnoreCase(name)){
                return tag;
            }
        }

        return null;
    }

    public static Tag getTagFromPrefix(String prefix){

        for(Tag tag : loadedTags){
            if(ChatUtils.format(tag.getPrefix().toLowerCase()).equals(ChatUtils.format(prefix.toLowerCase()))){
                return tag;
            }
        }

        return null;
    }
}
