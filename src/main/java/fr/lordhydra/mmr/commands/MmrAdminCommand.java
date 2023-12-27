package fr.lordhydra.mmr.commands;

import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.error.PlayerMMRNotFoundException;
import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.entity.Player;

public class MmrAdminCommand extends AbstractCommand {
    @Override
    protected Result mapPlayerAction(Player playerWhoExecutedTheCommand, String action, String[] args) {
        return switch (action) {
            case "add" -> addMmrToPlayer(args, false);
            case "del" -> addMmrToPlayer(args, true);
            default -> Result.error(Lang.unknownCommand);
        };
    }

    @Override
    protected Result mapTerminalAction(String action, String[] args) {
        return Result.error(Lang.cantBeExecuteByTerminal);
    }

    @Override
    protected void displayHelp(Player player) {

    }

    private Result addMmrToPlayer(String[] args, boolean negative) {
        if (args.length <= 1 ) {
            return Result.error(negative ? Lang.invalidDelCommand : Lang.invalidAddCommand);
        }
        ScoreService scoreService = new ScoreService();
        String targetPlayerName = args[0];
        String strMmrToAdd = args[1];
        double mmrToAdd;
        try {
            mmrToAdd = Double.parseDouble(strMmrToAdd);
        } catch (NumberFormatException e) {
            return Result.error(Lang.invalidMmrFormat);
        }

        if (mmrToAdd < 0) {
            return Result.error(negative ? Lang.delNegativeMmrParamError : Lang.addNegativeMmrParamError);
        }

        try {
            boolean result = scoreService.addMmrToPlayer(targetPlayerName, mmrToAdd, negative);
            String successMessage = negative ? Lang.delSuccessMessage : Lang.addSuccessMessage;
            String errorMessage = negative ? Lang.delErrorMessage : Lang.addErrorMessage;
            return result ? Result.ok(successMessage) : Result.error(errorMessage);

        } catch (PlayerMMRNotFoundException e) {
            return Result.error(Lang.playerNotFound);
        }
    }
}
