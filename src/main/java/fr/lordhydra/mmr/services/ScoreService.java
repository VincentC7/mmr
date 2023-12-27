package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.PlayerMMRNotFoundException;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ScoreService {

    public void applyMmrToPlayers(Player killer, Player killed) {
        BigDecimal killerMmr = BigDecimal.valueOf(getPlayerMmr(killer)); //P1
        BigDecimal killedMmr = BigDecimal.valueOf(getPlayerMmr(killed)); //P2

        //Calcul du nouveau MMR
        BigDecimal onDeathRate = BigDecimal.valueOf(Config.ON_DEATH_RATE); //A
        BigDecimal onKillRate = BigDecimal.valueOf(Config.ON_KILL_RATE); //B

        BigDecimal gainForKiller = killedMmr.multiply(onKillRate)
                .round(new MathContext(3, RoundingMode.HALF_UP)); // P2 * A
        BigDecimal loseForKilled = killedMmr.multiply(onDeathRate)
                .round(new MathContext(3, RoundingMode.HALF_UP)); // P2 * B

        BigDecimal killerNextMmr = killerMmr.add(gainForKiller); // P1 + P2 * A
        BigDecimal killedNextMmr = killedMmr.subtract(loseForKilled);// P2 + P2 * b

        savePlayerMmr(killer, killerNextMmr);
        savePlayerMmr(killed, killedNextMmr);

        //Debug
        killer.sendMessage("ton nouveau MMR :" + killerNextMmr);
        killed.sendMessage("ton nouveau MMR :" + killedNextMmr);
    }

    public double getPlayerMmr(Player player) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            return Config.DEFAULT_MMR;
        }
        return playerMmrEntity.mmr();
    }

    public double getPlayerMmr(String playerName) throws PlayerMMRNotFoundException {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerName);
        if (playerMmrEntity == null) {
            throw new PlayerMMRNotFoundException();
        }
        return playerMmrEntity.mmr();
    }

    public boolean addMmrToPlayer(String playerName, double mmr) throws PlayerMMRNotFoundException {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayerName(playerName);
        if (playerMmrEntity == null) {
            throw new PlayerMMRNotFoundException();
        }
        playerMmrEntity.addMmr(mmr);
        return playerMmrRepository.updatePlayerMmr(playerMmrEntity);
    }

    private void savePlayerMmr(Player player, BigDecimal newMmr) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            playerMmrRepository.insertPlayerMmr(player, newMmr);
        } else {
            playerMmrRepository.updatePlayerMmr(player, newMmr);
        }
    }
}
