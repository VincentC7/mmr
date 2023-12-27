package fr.lordhydra.mmr.commands;

import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.error.PlayerMMRNotFoundException;
import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MMRCommand extends AbstractCommand {

    @Override
    protected Result mapPlayerAction(Player player, String action, String[] args) {
        return switch (action) {
            case "rank" -> displayPlayerMMR(player, args);
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

    private Result displayPlayerMMR(Player player, String[] args) {
        ScoreService scoreService = new ScoreService();
        BigDecimal mmr;
        if (args.length == 0) {
            mmr = scoreService.getPlayerMmr(player);
            return Result.ok(Lang.currentPlayerMmrMessage.replace("{mmr}", mmr.toString()));
        }
        String playerName = args[0];
        Player targerPlayer = Bukkit.getPlayer(playerName);
        if (targerPlayer == null) {
            try {
                mmr = scoreService.getPlayerMmr(playerName);
            } catch (PlayerMMRNotFoundException e) {
                return Result.error(Lang.playerNotFound);
            }
        } else {
            mmr = scoreService.getPlayerMmr(targerPlayer);
        }
        return Result.ok(
                Lang.otherPLayerMmrMessage
                        .replace("{mmr}", mmr.toString())
                        .replace("{playerName}", playerName)
        );
    }
    
}
