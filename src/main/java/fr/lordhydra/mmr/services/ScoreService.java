package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import org.bukkit.entity.Player;

public class ScoreService {

    public void applyMmrToPlayers(Player killer, Player killed) {
        //TODO Récupérer le MMR des joueurs
        //getMMRkiller
        //getMMRkilled
        int killerMmr = 1000; //P1
        int killedMmr = 1000; //P2

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

}
