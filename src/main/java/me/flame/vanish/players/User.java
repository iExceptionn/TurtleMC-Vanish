package me.flame.vanish.players;

import org.bukkit.GameMode;

import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    private Boolean vanished;
    private String gamemode;
    private Boolean nightVision;

    public User(UUID uuid, String name, Boolean vanished, String gamemode, Boolean nightVision){
        this.uuid = uuid;
        this.name = name;
        this.vanished = vanished;
        this.gamemode = gamemode;
        this.nightVision = nightVision;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVanished() {
        return vanished;
    }

    public void setVanished(Boolean vanished) {
        this.vanished = vanished;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public Boolean getNightVision() {
        return nightVision;
    }

    public void setNightVision(Boolean nightVision) {
        this.nightVision = nightVision;
    }
}
