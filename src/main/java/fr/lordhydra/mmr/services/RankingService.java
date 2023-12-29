package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;

import java.util.ArrayList;

public class RankingService {

    public ArrayList<PlayerMmrEntity> fetchPlayerMmr(int page) {
        PlayerMmrRepository playerMmrRepository = new PlayerMmrRepository();
        return playerMmrRepository.getPlayersMmrs(page);
    }
}
