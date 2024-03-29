package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.entities.PlayerMmrStatus;
import fr.lordhydra.mmr.error.*;
import fr.lordhydra.mmr.lib.changeStatusTimer.ChangePlayerStatusTimer;
import fr.lordhydra.mmr.MMR;
import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import fr.lordhydra.mmr.lib.changeStatusTimer.ChangeStatusTimerPool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MmrStatusService {

    /**
     * Enable or disable MMR
     * @param player target
     */
    public void changePlayerMmrStatus(Player player, boolean disable) throws
            PlayerMmrAlreadyDisabled,
            StatusUpdateCouldown, playerHasAlreadyTimerStarted,
            PlayerMmrAlreadyActive,
            PlayerMmrBan
    {
        PlayerMmrEntity playerMmrEntity = getPlayerMmr(player);
        if (playerMmrEntity.status().equals(PlayerMmrStatus.BAN)) throw new PlayerMmrBan();
        if (playerMmrEntity.status().equals(PlayerMmrStatus.INACTIVE) && disable) throw new PlayerMmrAlreadyDisabled();
        if (playerMmrEntity.status().equals(PlayerMmrStatus.ACTIVE) && !disable) throw new PlayerMmrAlreadyActive();
        long playerCooldown = calculatePLayerCooldown(playerMmrEntity.statusUpdated());
        if (playerCooldown > 0) throw new StatusUpdateCouldown(playerCooldown);
        playerMmrEntity.status(disable ? PlayerMmrStatus.INACTIVE : PlayerMmrStatus.ACTIVE);
        ChangePlayerStatusTimer changePlayerStatusTimer = new ChangePlayerStatusTimer(
                player,
                playerMmrEntity,
                Config.PLAYER_CHANGE_STATUS_TIMER
        );
        ChangeStatusTimerPool.addTimer(player.getUniqueId(), changePlayerStatusTimer);
        changePlayerStatusTimer.runTaskTimer(MMR.getInstance(), 0, 20);
    }

    public void changePlayerMmrActivity(PlayerMmrEntity playerMmrEntity) {
        playerMmrEntity.statusUpdated(LocalDateTime.now());
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        playerMmrRepository.updatePlayerMmr(playerMmrEntity);
        ChangeStatusTimerPool.removeTimer(playerMmrEntity.playerUUID());
    }

    private PlayerMmrEntity getPlayerMmr(Player player) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            playerMmrEntity = PlayerMmrEntity.builder()
                    .playerUUID(player.getUniqueId())
                    .playerName(player.getName())
                    .mmr(BigDecimal.valueOf(Config.DEFAULT_MMR))
                    .mmrUpdated(LocalDateTime.now())
                    .status(PlayerMmrStatus.ACTIVE)
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

    public boolean checkPlayersStatusesValid(Player player1, Player player2) {
        PlayerMmrEntity player1Entity = getPlayerMmr(player1);
        PlayerMmrEntity player2Entity = getPlayerMmr(player2);
        return player1Entity.status().equals(PlayerMmrStatus.ACTIVE) &&
                player2Entity.status().equals(PlayerMmrStatus.ACTIVE);
    }

    public void banPlayerMmr(String playerName, boolean unban) throws PlayerMMRNotFoundException, PlayerMmrAlreadyBanned, PlayerMmrIsNotBan {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerName);
        if (playerMmrEntity == null) {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                throw new PlayerMMRNotFoundException();
            }
            playerMmrEntity = getPlayerMmr(player);
        }

        if (playerMmrEntity.status().equals(PlayerMmrStatus.BAN) && !unban) {
            throw new PlayerMmrAlreadyBanned();
        }

        if (!playerMmrEntity.status().equals(PlayerMmrStatus.BAN) && unban) {
            throw new PlayerMmrIsNotBan();
        }

        ChangeStatusTimerPool.removeTimer(playerMmrEntity.playerUUID());
        playerMmrEntity.status(unban ? PlayerMmrStatus.INACTIVE : PlayerMmrStatus.BAN);
        playerMmrEntity.statusUpdated(LocalDateTime.now());
        playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }

    public void displayPlayerMmrInfoToAdmin(Player adminPlayer, String playerSearched) throws PlayerMMRNotFoundException {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerSearched);
        if (playerMmrEntity == null) {
            Player player = Bukkit.getPlayer(playerSearched);
            if (player == null) {
                throw new PlayerMMRNotFoundException();
            }
            playerMmrEntity = getPlayerMmr(player);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy à HH:mm:ss");
        String info = ChatColor.GRAY + "------------------ " +
                ChatColor.AQUA + "Info " + playerMmrEntity.playerName() +
                ChatColor.GRAY + " ------------------\n" +
                ChatColor.WHITE + "- MMR : " + ChatColor.YELLOW + playerMmrEntity.mmr() + "\n" +
                ChatColor.WHITE + "- Dernier changement de mmr : " + ChatColor.YELLOW + playerMmrEntity.mmrUpdated().format(dateTimeFormatter) + "\n" +
                ChatColor.WHITE + "- status : " + ChatColor.YELLOW + playerMmrEntity.status().getDbName() + "\n" +
                ChatColor.WHITE + "- Dernière modification du status : " + ChatColor.YELLOW + playerMmrEntity.statusUpdated().format(dateTimeFormatter) + "\n" +
                ChatColor.GRAY + "----------------------------------------------------";
        adminPlayer.sendMessage(info);
    }

    public List<String> fetchBannedPlayerNameList() {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        return playerMmrRepository.fetchBannedPlayerNameList();
    }
}
