package fr.lordhydra.mmr.entities;

import lombok.Getter;

public enum PlayerMmrStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    BAN("ban");

    PlayerMmrStatus (String dbName) {
        this.dbName = dbName;
    }

    @Getter
    final String dbName;

    public static PlayerMmrStatus fromString(String text) {
        for (PlayerMmrStatus playerMmrStatus : PlayerMmrStatus.values()) {
            if (playerMmrStatus.dbName.equalsIgnoreCase(text)) {
                return playerMmrStatus;
            }
        }
        return null;
    }

}
