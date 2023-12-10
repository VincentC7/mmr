package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;
import org.bukkit.entity.Player;

public class ScoreService {

    public void applyMmrToPlayers(Player killer, Player killed) {
        double killerMmr = getPlayerMmr(killer); //P1
        double killedMmr = getPlayerMmr(killed); //P2

        //Calcul du nouveau MMR
        Double onDeathRate = Config.ON_DEATH_RATE; //A
        Double onKillRate = Config.ON_KILL_RATE; //B
        double killerNextMmr = killerMmr + killedMmr * onKillRate;
        double killedNextMmr = killedMmr + killerMmr * onDeathRate;

        //Todo Sauvegarder le nouveau MMR en base
        //saveMMR(killer, killerNextMmr)
        //saveMMR(killed, killedNextMmr)

        //Debug
        killer.sendMessage("ton nouveau MMR :" + killerNextMmr);
        killed.sendMessage("ton nouveau MMR :" + killedNextMmr);
    }

    private double getPlayerMmr(Player player) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        PlayerMmrEntity playerMmrEntity = playerMmrRepository.findByPlayer(player);
        if (playerMmrEntity == null) {
            return Config.DEFAULT_MMR;
        }
        return playerMmrEntity.mmr();
    }
}
