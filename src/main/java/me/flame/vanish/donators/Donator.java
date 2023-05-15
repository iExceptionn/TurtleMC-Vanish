package me.flame.vanish.donators;

import me.flame.vanish.donators.managers.DonatorManager;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Donator {

    private final DonatorManager donatorManager = new DonatorManager();

    private String name;
    private String displayName;
    private UUID uuid;
    private String prefix;
    private Date date;
    private List<String> unlockedTags;
    private String currentTag;

    public Donator(UUID uuid, String name, String displayName, String prefix, Date date, List<String> unlockedTags, String currentTag){
        this.uuid = uuid;
        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        this.date = date;
        this.unlockedTags = unlockedTags;
        this.currentTag = currentTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getUnlockedTags() {
        return unlockedTags;
    }

    public void setUnlockedTags(List<String> unlockedTags) {
        this.unlockedTags = unlockedTags;
    }

    public String getCurrentTag() {
        return currentTag;
    }

    public void setCurrentTag(String currentTag) {
        this.currentTag = currentTag;
    }
}
