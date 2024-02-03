package fr.lordhydra.mmr;

import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@AllArgsConstructor
public class ChangePlayerStatusTimer extends BukkitRunnable {
    
    private PlayerMmrEntity playerMmrEntity;
    private int currentTimer;

    @Override
    public void run() {
        if (currentTimer == 0) {

        }
        currentTimer--;
    }

}
