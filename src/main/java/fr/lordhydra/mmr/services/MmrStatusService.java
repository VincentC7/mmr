package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.PlayerMmrAlreadyActive;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MmrStatusService {

    /**
     * Enable the player to win or lose MMRs
     * @param player target
     */
    public void enablePlayerMmr(Player player) throws PlayerMmrAlreadyActive {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            playerMmrRepository.insertPlayerMmr(PlayerMmrEntity.builder()
                    .playerUUID(player.getUniqueId())
                    .playerName(player.getName())
                    .mmr(BigDecimal.valueOf(Config.DEFAULT_MMR))
                    .build());
            return;
        }
        if (playerMmrEntity.isActive()) {
            throw new PlayerMmrAlreadyActive();
        }
        playerMmrEntity.isActive(true);
        playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }
}
