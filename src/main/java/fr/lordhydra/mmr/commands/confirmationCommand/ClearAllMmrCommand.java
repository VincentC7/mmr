package fr.lordhydra.mmr.commands.confirmationCommand;

import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.entity.Player;

public class ClearAllMmrCommand extends AbstractConfirmCommand {

    public ClearAllMmrCommand(Player player) {
        super(player);
    }

    @Override
    public Result executeConfirm() {
        ScoreService scoreService = new ScoreService();
        scoreService.resetAllPlayerMmr();
        return Result.ok("Vous venez de reset le mmr de tous les joueurs");
    }

    @Override
    public String getConfirmationCommandExample() {
        return "/mmra clearAll confirm";
    }

    @Override
    public String getPrepareSuccesMessage() {
        return "Vous etes sous le point de reset le MMR de tous les joueurs, pour valider cette opération tapez /mmr resetAll confirm";
    }

}
