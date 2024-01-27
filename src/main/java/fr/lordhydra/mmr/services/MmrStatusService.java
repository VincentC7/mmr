package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.PlayerMmrAlreadyActive;
import fr.lordhydra.mmr.error.PlayerMmrAlreadyDisabled;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MmrStatusService {

    /**
     * Enable the player to win or lose MMRs
     * @param player target
     */
    public void enablePlayerMmr(Player player) throws PlayerMmrAlreadyActive {
        PlayerMmrEntity playerMmrEntity = getPlayerMmr(player);
        if (playerMmrEntity.isActive()) {
            throw new PlayerMmrAlreadyActive();
        }
        playerMmrEntity.isActive(true);
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }

    public void disablePLayerMmr(Player player) throws PlayerMmrAlreadyDisabled {
        PlayerMmrEntity playerMmrEntity = getPlayerMmr(player);
        if (!playerMmrEntity.isActive()) {
            throw new PlayerMmrAlreadyDisabled();
        }
        playerMmrEntity.isActive(false);
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
}
