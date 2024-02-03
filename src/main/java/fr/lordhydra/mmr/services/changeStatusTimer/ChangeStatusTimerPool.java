package fr.lordhydra.mmr.services.changeStatusTimer;

import fr.lordhydra.mmr.error.playerHasAlreadyTimerStarted;
import fr.lordhydra.mmr.utils.Logger;

import java.util.HashMap;
import java.util.UUID;

public class ChangeStatusTimerPool {

    private static final HashMap<UUID, ChangePlayerStatusTimer> changeStatusTimerPool = new HashMap<>();

    public static void addTimer(UUID playerUUID, ChangePlayerStatusTimer changePlayerStatusTimer) throws playerHasAlreadyTimerStarted {
        if (changeStatusTimerPool.containsKey(playerUUID)) {
            throw new playerHasAlreadyTimerStarted(changePlayerStatusTimer.getCurrentTimer());
        }
        changeStatusTimerPool.put(playerUUID, changePlayerStatusTimer);
    }

    public static void removeTimer(UUID playerUUID) {
        changeStatusTimerPool.remove(playerUUID);
    }

}
