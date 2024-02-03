package fr.lordhydra.mmr.services.changeStatusTimer;

import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.services.MmrStatusService;
import fr.lordhydra.mmr.utils.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.lordhydra.mmr.commands.AbstractCommand.PLUGIN_PREFIX;
import static org.bukkit.ChatColor.GREEN;

@AllArgsConstructor
public class ChangePlayerStatusTimer extends BukkitRunnable {

    private Player player;
    private PlayerMmrEntity playerMmrEntity;
    @Getter
    private int currentTimer;

    @Override
    public void run() {
        if (currentTimer == 0) {
            MmrStatusService mmrStatusService = new MmrStatusService();
            mmrStatusService.changePlayerMmrActivity(playerMmrEntity);
            player.sendMessage(PLUGIN_PREFIX + GREEN + (
                    playerMmrEntity.isActive() ?
                            Lang.playerMmrIsNowActive :
                            Lang.playerMmrIsNowDisable
                    )
            );
            cancel();
        }
        currentTimer--;
    }

}
