package fr.lordhydra.mmr.commands;

import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MMRCommand extends AbstractCommand {

    @Override
    protected Result mapPlayerAction(Player player, String action, String[] args) {
        return switch (action) {
            case "rank" -> displayPlayerMMR(player);
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

    private Result displayPlayerMMR(Player player) {
        ScoreService scoreService = new ScoreService();
        BigDecimal mmr = BigDecimal.valueOf(scoreService.getPlayerMmr(player));
        return Result.ok("Actuellement, tu as un MMR de : " + mmr);
    }
    
}
