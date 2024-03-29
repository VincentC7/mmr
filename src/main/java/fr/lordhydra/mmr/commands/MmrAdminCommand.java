package fr.lordhydra.mmr.commands;

import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.PlayerMMRNotFoundException;
import fr.lordhydra.mmr.error.PlayerMmrAlreadyBanned;
import fr.lordhydra.mmr.error.PlayerMmrIsNotBan;
import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.services.MmrStatusService;
import fr.lordhydra.mmr.services.ScoreService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

public class MmrAdminCommand extends AbstractCommand {
    @Override
    protected Result mapPlayerAction(Player playerWhoExecutedTheCommand, String action, String[] args) {
        return switch (action) {
            case "add" -> addMmrToPlayer(args, false);
            case "del" -> addMmrToPlayer(args, true);
            case "reset" -> resetPlayerMmr(args);
            case "ban" -> banPlayerMmr(args, false);
            case "unban" -> banPlayerMmr(args, true);
            case "info" -> displayPlayerInfo(playerWhoExecutedTheCommand, args);
            case "banlist" -> displayBannedPlayerList(playerWhoExecutedTheCommand, args);
            default -> Result.error(Lang.unknownCommand);
        };
    }

    @Override
    protected Result mapTerminalAction(String action, String[] args) {
        return Result.error(Lang.cantBeExecuteByTerminal);
    }

    @Override
    protected void buildCommandList() {
        commands = new HashSet<>();
        commands.add(new HelpCommand(Lang.addSampleCommand, Lang.addCommandDescription));
        commands.add(new HelpCommand(Lang.delSampleCommand, Lang.delCommandDescription));
        commands.add(new HelpCommand(Lang.resetSampleCommand, Lang.resetCommandDescription));
        commands.add(new HelpCommand(Lang.banSampleCommand, Lang.banCommandDescription));
        commands.add(new HelpCommand(Lang.unbanSampleCommand, Lang.unbanCommandDescription));
        commands.add(new HelpCommand(Lang.banlistSampleCommand, Lang.banlistCommandDescription));
    }

    private Result addMmrToPlayer(String[] args, boolean negative) {
        if (args.length <= 1 ) {
            return Result.error(negative ? Lang.invalidDelCommand : Lang.invalidAddCommand);
        }
        ScoreService scoreService = new ScoreService();
        String targetPlayerName = args[0];
        String strMmrToAdd = args[1];
        BigDecimal mmrToAdd;
        try {
            mmrToAdd = new BigDecimal(strMmrToAdd);
        } catch (NumberFormatException e) {
            return Result.error(Lang.invalidMmrFormat);
        }

        if (mmrToAdd.compareTo(BigDecimal.ZERO) < 0) {
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

    private Result resetPlayerMmr(String[] args) {
        if (args.length == 0) {
            return Result.error(Lang.invalidResetCommand);
        }
        String playerName = args[0];
        ScoreService scoreService = new ScoreService();
        try {
            scoreService.resetPlayerMmr(playerName);
        } catch (PlayerMMRNotFoundException e) {
            return Result.error(Lang.playerNotFound);
        }
        return Result.ok(Lang.resetSuccessMessage);
    }

    private Result banPlayerMmr(String[] args, boolean unban) {
        if (args.length == 0) {
            return Result.error(unban ? Lang.invalidunbanCommand : Lang.invalidBanCommand);
        }
        String playerName = args[0];
        MmrStatusService mmrStatusService = new MmrStatusService();
        try {
            mmrStatusService.banPlayerMmr(playerName, unban);
        } catch (PlayerMMRNotFoundException e) {
            return Result.error(Lang.playerNotFound);
        } catch (PlayerMmrAlreadyBanned e) {
            return Result.error(Lang.playerMmrAlreadyBan);
        } catch (PlayerMmrIsNotBan e) {
            return Result.error(Lang.playerMmrIsNotBan);
        }
        return Result.ok(unban ? Lang.unbanSuccessMessage : Lang.banSuccessMessage);
    }

    private Result displayPlayerInfo(Player adminPlayer, String[] args) {
        if (args.length == 0) {
            return Result.error(Lang.invalidPlayerInfoCommand);
        }
        String playerSearched = args[0];
        MmrStatusService mmrStatusService = new MmrStatusService();
        try {
            mmrStatusService.displayPlayerMmrInfoToAdmin(adminPlayer, playerSearched);
        } catch (PlayerMMRNotFoundException e) {
            return Result.error(Lang.playerNotFound);
        }
        return Result.ok();
    }

    private Result displayBannedPlayerList(Player adminPlayer, String[] args) {
        if (args.length != 0) {
            return Result.error(Lang.tooManyArgument);
        }
        MmrStatusService mmrStatusService = new MmrStatusService();
        List<String> playerNameList = mmrStatusService.fetchBannedPlayerNameList();
        if (playerNameList.isEmpty()) {
            return Result.ok(Lang.bannedListEmpty);
        }
        String stringBuilder = ChatColor.GRAY + "------------------ " +
                ChatColor.AQUA + "Joueurs Bannis" +
                ChatColor.GRAY + " -------------------" +
                "\n" +
                ChatColor.WHITE +
                String.join(", ", playerNameList) + "\n" +
                ChatColor.GRAY + "----------------------------------------------------";
        adminPlayer.sendMessage(stringBuilder);
        return Result.ok();
    }

}
