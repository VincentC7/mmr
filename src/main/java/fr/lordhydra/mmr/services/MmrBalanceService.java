package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.MmrBalanceEntity;
import fr.lordhydra.mmr.repository.MmrBalanceRepository;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.UUID;

public class MmrBalanceService {

    public MmrBalanceEntity getMmrBalance(Player killer, Player killed) {
        MmrBalanceRepository mmrBalanceRepository = new MmrBalanceRepository();
        MmrBalanceEntity mmrBalanceEntity = mmrBalanceRepository.findByPlayers(killer, killed);
        if (mmrBalanceEntity == null) {
            mmrBalanceEntity = createMmrBalance(killer,killed);
        }
        return mmrBalanceEntity;
    }

    private MmrBalanceEntity createMmrBalance(Player killer, Player killed) {
        MmrBalanceEntity mmrBalanceEntity = MmrBalanceEntity.builder()
                .firstPlayerUUID(killer.getUniqueId())
                .secondPlayerUUID(killed.getUniqueId())
                .balance(0)
                .build();
        MmrBalanceRepository mmrBalanceRepository = new MmrBalanceRepository();
        mmrBalanceRepository.insert(mmrBalanceEntity);
        return mmrBalanceEntity;
    }

    public BigDecimal getMmrBalanceRateModifier(MmrBalanceEntity mmrBalanceEntity, UUID killerUUID) {
        //Si la balance est négative, alors c'est le deuxième joueur qui a le plus kill le premier joueur
        //Sinon c'est le premier qui a kill le plus le deuxième
        if (killerUUID.equals(mmrBalanceEntity.firstPlayerUUID()) && mmrBalanceEntity.balance() < 0 ||
                killerUUID.equals(mmrBalanceEntity.secondPlayerUUID()) && mmrBalanceEntity.balance() > 0
        ) {
            return BigDecimal.ONE;
        }
        int balance = Math.abs(mmrBalanceEntity.balance());
        if (balance > Config.BALANCE_SIZE ) {
            return BigDecimal.ZERO;
        }
        //max/2 ≤ b ≤ max ⇒ 1 - (b - max/2) / (max - max/2)
        if (balance > Config.BALANCE_SIZE/2) {
            BigDecimal balanceSize = BigDecimal.valueOf(Config.BALANCE_SIZE);
            BigDecimal halfBalanceSize = balanceSize.divide(BigDecimal.valueOf(2), RoundingMode.DOWN);
            BigDecimal denominator = balanceSize.subtract(halfBalanceSize);
            BigDecimal numerator = new BigDecimal(balance).subtract(halfBalanceSize);

            return BigDecimal.ONE.subtract(
                    numerator.divide(denominator, new MathContext(3, RoundingMode.HALF_UP))
            );
        }
        return BigDecimal.ONE;
    }

    public void updateBalance(MmrBalanceEntity mmrBalanceEntity, Player killer) {
        MmrBalanceRepository mmrBalanceRepository = new MmrBalanceRepository();
        int balancePlayer1 = mmrBalanceEntity.balance();
        if (mmrBalanceEntity.firstPlayerUUID().equals(killer.getUniqueId())) {
            mmrBalanceEntity.balance(balancePlayer1 + 1);
        } else {
            mmrBalanceEntity.balance(balancePlayer1 - 1);
        }
        mmrBalanceRepository.update(mmrBalanceEntity);
    }
}
