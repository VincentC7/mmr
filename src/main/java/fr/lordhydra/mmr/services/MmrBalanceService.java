package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.MmrBalanceEntity;
import fr.lordhydra.mmr.repository.MmrBalanceRepository;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MmrBalanceService {

    public BigDecimal getMmrBalanceValue(Player killer, Player killed) {
        MmrBalanceRepository mmrBalanceRepository = new MmrBalanceRepository();
        MmrBalanceEntity mmrBalanceEntity = mmrBalanceRepository.findByPlayers(killer, killed);
        if (mmrBalanceEntity == null) {
            mmrBalanceEntity = createMmrBalance(killer,killed);
        }
        int balancePlayer1 = Math.abs(mmrBalanceEntity.balancePlayer1());
        int balancePlayer2 = Math.abs(mmrBalanceEntity.balancePlayer2());

        if (balancePlayer1 > Config.BALANCE_SIZE || balancePlayer2 > Config.BALANCE_SIZE) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ONE;
    }

    private MmrBalanceEntity createMmrBalance(Player killer, Player killed) {
        MmrBalanceEntity mmrBalanceEntity = MmrBalanceEntity.builder()
                .firstPlayerUUID(killer.getUniqueId())
                .secondPlayerUUID(killed.getUniqueId())
                .balancePlayer1(0)
                .balancePlayer2(0)
                .build();
        MmrBalanceRepository mmrBalanceRepository = new MmrBalanceRepository();
        mmrBalanceRepository.insert(mmrBalanceEntity);
        return mmrBalanceEntity;
    }

}
