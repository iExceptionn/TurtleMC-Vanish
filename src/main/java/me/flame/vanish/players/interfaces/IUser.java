package me.flame.vanish.players.interfaces;

import java.util.UUID;

public interface IUser {

    void createStaff(UUID uuid);

    void loadStaff(UUID uuid);

    void saveStaff(UUID uuid);

    void vanishPlayer(UUID uuid, Boolean message);

    void unVanishPlayer(UUID uuid, Boolean message);

    Boolean isVanished(UUID uuid);



}
