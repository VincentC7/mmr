package fr.lordhydra.mmr.listeners;

import fr.lordhydra.mmr.entities.MmrBalanceEntity;
import fr.lordhydra.mmr.services.MmrBalanceService;
import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent e)
    {
        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            MmrBalanceService mmrBalanceService = new MmrBalanceService();
            MmrBalanceEntity mmrBalanceEntity = mmrBalanceService.getMmrBalance(killer, killed);
            BigDecimal mmrBalanceValue = mmrBalanceService.getMmrBalanceValue(mmrBalanceEntity);
            new ScoreService().applyMmrToPlayers(killer, killed, mmrBalanceValue);
            mmrBalanceService.updateBalance(mmrBalanceEntity, killer);
        }
    }
}
