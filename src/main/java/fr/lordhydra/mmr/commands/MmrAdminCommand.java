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
            String validCommand = "/mmr" + (negative ? "add" : "del") + "nomDuJoueur add 1000";
            return Result.error("La commande n'est pas bonne. Voici un exemple valide : " + validCommand);
        }
        ScoreService scoreService = new ScoreService();
        String targetPlayerName = args[0];
        String strMmrToAdd = args[1];
        double mmrToAdd;
        try {
            mmrToAdd = Double.parseDouble(strMmrToAdd);
        } catch (NumberFormatException e) {
            return Result.error("Le mmr que tu souhaites ajouter au joueur doit être sous la forme 100 ou 150.50");
        }

        if (mmrToAdd < 0) {
            return Result.error("Le mmr que tu souhaites " + (negative ? "retirer" : "ajouter") + " doit être strictement positif");
        }

        try {
            boolean result = scoreService.addMmrToPlayer(targetPlayerName, mmrToAdd, negative);
            String message = "Le mmr a bien été " + (negative ? "retiré" : "ajouté") + " au joueur";
            return result ? Result.ok(message) :
                    Result.error("Une erreur est survenue lors de l'ajout du mmr au joueur");

        } catch (PlayerMMRNotFoundException e) {
            return Result.error("Le joueur n'a pas été trouvé");
        }
    }
}
