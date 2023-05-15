package me.flame.vanish.tags;

import java.util.List;

public class Tag {

    private String name;
    private String prefix;
    private List<String> lore;
    private boolean visible;
    private boolean enabled;

    public Tag(String name, String prefix, List<String> lore, boolean visible, boolean enabled){

        this.name = name;
        this.prefix = prefix;
        this.lore = lore;
        this.visible = visible;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }
}
