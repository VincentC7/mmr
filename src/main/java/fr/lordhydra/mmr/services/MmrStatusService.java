package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.ChangePlayerStatusTimer;
import fr.lordhydra.mmr.MMR;
import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.PlayerMmrAlreadyActive;
import fr.lordhydra.mmr.error.PlayerMmrAlreadyDisabled;
import fr.lordhydra.mmr.error.StatusUpdateCouldown;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MmrStatusService {

    /**
     * Enable the player to win or lose MMRs
     * @param player target
     */
    public void enablePlayerMmr(Player player) throws PlayerMmrAlreadyActive, StatusUpdateCouldown {
        PlayerMmrEntity playerMmrEntity = getPlayerMmr(player);
        if (playerMmrEntity.isActive()) throw new PlayerMmrAlreadyActive();
        long playerCooldown = calculatePLayerCooldown(playerMmrEntity.statusUpdated());
        if (playerCooldown > 0) throw new StatusUpdateCouldown(playerCooldown);
        playerMmrEntity.isActive(true);
        ChangePlayerStatusTimer changePlayerStatusTimer = new ChangePlayerStatusTimer(
                playerMmrEntity,
                Config.PLAYER_CHANGE_STATUS_TIMER
        );
        changePlayerStatusTimer.runTaskTimer(MMR.getInstance(), 0, 20);
    }

    public void disablePLayerMmr(Player player) throws PlayerMmrAlreadyDisabled, StatusUpdateCouldown {
        PlayerMmrEntity playerMmrEntity = getPlayerMmr(player);
        if (!playerMmrEntity.isActive()) throw new PlayerMmrAlreadyDisabled();
        long playerCooldown = calculatePLayerCooldown(playerMmrEntity.statusUpdated());
        if (playerCooldown > 0) throw new StatusUpdateCouldown(playerCooldown);
        playerMmrEntity.isActive(false);
        ChangePlayerStatusTimer changePlayerStatusTimer = new ChangePlayerStatusTimer(
                playerMmrEntity,
                Config.PLAYER_CHANGE_STATUS_TIMER
        );
        changePlayerStatusTimer.runTaskTimer(MMR.getInstance(), 0, 20);
    }

    public void changePlayerMmrActivity(PlayerMmrEntity playerMmrEntity) {
        playerMmrEntity.statusUpdated(LocalDateTime.now());
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }

    private PlayerMmrEntity getPlayerMmr(Player player) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            playerMmrEntity = PlayerMmrEntity.builder()
                    .playerUUID(player.getUniqueId())
                    .playerName(player.getName())
                    .mmr(BigDecimal.valueOf(Config.DEFAULT_MMR))
                    .isActive(true)
                    .build();
            playerMmrRepository.insertPlayerMmr(playerMmrEntity);
        }
        return playerMmrEntity;
    }

    /**
     * Calculate if player is in cooldown and return the timer left
     * @param timeFromLastStatusUpdate time from last update
     * @return time left if he is on CD or 0 if he isn't
     */
    private long calculatePLayerCooldown(LocalDateTime timeFromLastStatusUpdate) {
        LocalDateTime tempDateTime = LocalDateTime.from(timeFromLastStatusUpdate);
        return Config.STATUS_UPDATE_COOLDOWN - tempDateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }
}
