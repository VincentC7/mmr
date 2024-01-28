package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.PlayerMMRNotFoundException;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

public class ScoreService {

    public void applyMmrToPlayers(Player killer, Player killed, BigDecimal mmrBalanceRateModifier) {
        if (mmrBalanceRateModifier.equals(BigDecimal.ZERO)) return;
        BigDecimal killerMmr = getPlayerMmr(killer); //P1
        BigDecimal killedMmr = getPlayerMmr(killed); //P2

        //Calcul du nouveau MMR
        BigDecimal onDeathRate = BigDecimal.valueOf(Config.ON_DEATH_RATE); //A
        BigDecimal onKillRate = BigDecimal.valueOf(Config.ON_KILL_RATE); //B

        BigDecimal gainForKiller = killedMmr
                .multiply(onKillRate)
                .multiply(mmrBalanceRateModifier)
                .round(new MathContext(3, RoundingMode.HALF_UP)); // P2 * A
        BigDecimal loseForKilled = killedMmr
                .multiply(onDeathRate)
                .multiply(mmrBalanceRateModifier)
                .round(new MathContext(3, RoundingMode.HALF_UP)); // P2 * B

        BigDecimal killerNextMmr = killerMmr.add(gainForKiller); // P1 + P2 * A
        BigDecimal killedNextMmr = killedMmr.subtract(loseForKilled);// P2 + P2 * b

        savePlayerMmr(killer, killerNextMmr);
        savePlayerMmr(killed, killedNextMmr);

        //Debug
        killer.sendMessage("ton nouveau MMR :" + killerNextMmr);
        killed.sendMessage("ton nouveau MMR :" + killedNextMmr);
    }

    public BigDecimal getPlayerMmr(Player player) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            return BigDecimal.valueOf(Config.DEFAULT_MMR);
        }
        return playerMmrEntity.mmr();
    }

    public BigDecimal getPlayerMmr(String playerName) throws PlayerMMRNotFoundException {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerName);
        if (playerMmrEntity == null) {
            throw new PlayerMMRNotFoundException();
        }
        return playerMmrEntity.mmr();
    }

    public boolean addMmrToPlayer(String playerName, BigDecimal mmr, boolean negative) throws PlayerMMRNotFoundException {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerName);
        if (playerMmrEntity == null) {
            throw new PlayerMMRNotFoundException();
        }
        playerMmrEntity.addMmr(negative ? mmr.multiply(BigDecimal.valueOf(-1)) : mmr);
        return playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }

    private void savePlayerMmr(Player player, BigDecimal newMmr) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            playerMmrEntity = PlayerMmrEntity.builder()
                    .playerUUID(player.getUniqueId())
                    .playerName(player.getName())
                    .mmr(newMmr)
                    .build();
            playerMmrRepository.insertPlayerMmr(playerMmrEntity);
        } else {
            playerMmrEntity.mmr(newMmr);
            playerMmrEntity.mmrUpdated(new Date());
            playerMmrRepository.updatePlayerMmr(playerMmrEntity);
        }
    }

    public void resetPlayerMmr(String playerName) throws PlayerMMRNotFoundException {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerName);
        if (playerMmrEntity == null) {
            throw new PlayerMMRNotFoundException();
        }
        playerMmrEntity.mmr(BigDecimal.valueOf(Config.DEFAULT_MMR));
        playerMmrEntity.mmrUpdated(new Date());
        playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }
}
