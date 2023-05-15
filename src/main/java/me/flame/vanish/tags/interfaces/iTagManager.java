package me.flame.vanish.tags.interfaces;

import me.flame.vanish.donators.Donator;
import me.flame.vanish.players.User;
import me.flame.vanish.tags.Tag;

public interface iTagManager {

    void loadTags();
    void createTag(String name, String prefix, String[] lore, boolean visble, boolean enabled);
    boolean tagIdUsed(String name);
    void deleteTag(Tag tag);
    void setTag(Donator donator, String prefix);

    boolean hasTag(Donator donator, Tag tag);
    void addTag(Donator donator);
    void removeTag(Donator donator);
}
