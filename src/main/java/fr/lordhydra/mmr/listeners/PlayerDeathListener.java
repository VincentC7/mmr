package fr.lordhydra.mmr.listeners;

import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent e)
    {
        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            new ScoreService().applyMmrToPlayers(killer, killed);
        }
    }
}
