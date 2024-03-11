package fr.lordhydra.mmr.commands.confirmationCommand;

import fr.lordhydra.mmr.MMR;
import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.error.playerHasAlreadyConfirmationCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractConfirmCommand extends BukkitRunnable implements ConfirmationCommand {

    public Player player;
    BukkitTask bukkitTask;

    protected AbstractConfirmCommand(Player player) {
        this.player = player;
    }

    public abstract Result executeConfirm();
    public abstract String getConfirmationCommandExample();
    public abstract String getPrepareSuccesMessage();

    @Override
    public Result prepare()  {
        try {
            ConfirmationCommandPool.addConfirmationRequest(player.getUniqueId(),this);
            bukkitTask = this.runTaskLater(MMR.getInstance(), 20 * 20);
            return Result.ok(getPrepareSuccesMessage());
        } catch (playerHasAlreadyConfirmationCommand e) {
            return Result.error(ChatColor.RED + "Une demande de confirmation est déjà en cours faite " +
                    getConfirmationCommandExample() + "pour valider l'action");
        }
    }

    @Override
    public boolean check() {
        return ConfirmationCommandPool.contains(player.getUniqueId());
    }

    @Override
    public Result confirm() {
        //A ce moment là c'est pas this parce qu'on a add l'ancienne instance de AbstractConfirmCommand dans la pool
        AbstractConfirmCommand request = ConfirmationCommandPool.getConfirmationCommand(player.getUniqueId());
        ConfirmationCommandPool.removeConfirmCommand(player.getUniqueId());
        if (request.bukkitTask != null && !request.bukkitTask.isCancelled()) {
            request.bukkitTask.cancel();
        }
        return executeConfirm();
    }

    @Override
    public void run() {
        ConfirmationCommandPool.removeConfirmCommand(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "Le temps de confirmation est arrivé à expiration");
    }

}
