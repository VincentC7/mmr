package fr.lordhydra.mmr.commands;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.config.Ranks;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.error.*;
import fr.lordhydra.mmr.lib.rank.Rank;
import fr.lordhydra.mmr.services.MmrStatusService;
import fr.lordhydra.mmr.services.RankingService;
import fr.lordhydra.mmr.services.ScoreService;
import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.utils.MessageColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;

public class MmrPlayerCommand extends AbstractCommand {

    @Override
    protected Result mapPlayerAction(Player player, String action, String[] args) {
        return switch (action) {
            case "rank" -> displayPlayerMMR(player, args);
            case "top" -> displayTopPlayerMMR(player, args);
            case "on" -> changePlayerMmrStatus(player, args, false);
            case "off" -> changePlayerMmrStatus(player, args, true);
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
        commands.add(new HelpCommand(Lang.currentPlayerSampleCommand, Lang.currentPlayerCommandDescription));
        commands.add(new HelpCommand(Lang.otherPlayerSampleCommand, Lang.otherPlayerCommandDescription));
        commands.add(new HelpCommand(Lang.topSampleCommand, Lang.topCommandDescription));
        commands.add(new HelpCommand(Lang.enableMmrSampleCommand, Lang.enableMmrCommandDescription));
        commands.add(new HelpCommand(Lang.disableMmrSampleCommand, Lang.disableMmrCommandDescription));
    }

    private Result displayPlayerMMR(Player player, String[] args) {
        ScoreService scoreService = new ScoreService();
        BigDecimal mmr;
        if (args.length == 0) {
            mmr = scoreService.getPlayerMmr(player);
            Rank rank = Ranks.getInstance().getRank(mmr.intValue());
            return Result.ok(Lang.currentPlayerMmrMessage
                    .replace("{mmr}", mmr.toString())
                    .replace("{rank}", MessageColor.injectColors(rank.getName()))
            );
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
        Rank rank = Ranks.getInstance().getRank(mmr.intValue());
        return Result.ok(
                Lang.otherPLayerMmrMessage
                        .replace("{mmr}", mmr.toString())
                        .replace("{playerName}", playerName)
                        .replace("{rank}", MessageColor.injectColors(rank.getName()))
        );
    }

    private Result displayTopPlayerMMR(Player player, String[] args) {
        int page = 1;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return Result.error(Lang.invalidTopCommand);
            }
        }
        RankingService rankingService = new RankingService();
        ArrayList<PlayerMmrEntity> playerMmrEntities = rankingService.fetchPlayerMmr(page);
        if (playerMmrEntities.isEmpty()) {
            return Result.ok("Aucun résultat");
        }
        player.sendMessage(buildTopMmrString(playerMmrEntities, page));
        return Result.ok();
    }

    private String buildTopMmrString(ArrayList<PlayerMmrEntity> playerMmrEntities, int page) {
        RankingService rankingService = new RankingService();
        int pageCount = BigDecimal.valueOf((double) rankingService.countPlayerMmr() / Config.TOP_MMR_PAGE_SIZE).setScale(0, RoundingMode.CEILING).intValue();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(ChatColor.YELLOW).append(" ---- ")
                .append(ChatColor.GOLD).append("top MMR")
                .append(ChatColor.YELLOW).append(" -- ")
                .append(ChatColor.GOLD).append("Page ")
                .append(ChatColor.DARK_RED).append(page)
                .append(ChatColor.GOLD).append("/")
                .append(ChatColor.DARK_RED).append(pageCount)
                .append(ChatColor.YELLOW).append(" ---- ")
                .append("\n")
                .append(ChatColor.WHITE);
        int top = 1;
        for (PlayerMmrEntity playerMmrEntity : playerMmrEntities) {
            Rank rank = Ranks.getInstance().getRank(playerMmrEntity.mmr().intValue());
            stringBuilder
                    .append(top++ + (Config.TOP_MMR_PAGE_SIZE * (page - 1))).append(". ")
                    .append(playerMmrEntity.playerName())
                    .append(" : ")
                    .append(MessageColor.injectColors(rank.getName()))
                    .append(" ").append(ChatColor.WHITE)
                    .append(playerMmrEntity.mmr())
                    .append("\n");
        }
        if (page != pageCount) {
            stringBuilder
                    .append(ChatColor.GOLD)
                    .append(Lang.topNextPageMessage.replace(
                    "{command}",
                    ChatColor.RED + "/mmr top " + (page+1) + ChatColor.GOLD
            ));
        }
        return stringBuilder.toString();
    }

    private Result changePlayerMmrStatus(Player player, String[] args, boolean disable) {
        if (args.length != 0) {
            return Result.error(Lang.tooManyArgument);
        }
        MmrStatusService mmrStatusService = new MmrStatusService();
        try {
            mmrStatusService.changePlayerMmrStatus(player, disable);
        } catch (PlayerMmrBan e) {
            return Result.error(Lang.playerMmrIsBan);
        } catch (PlayerMmrAlreadyDisabled e) {
            return Result.error(Lang.playerMmrAlreadyDisabled);
        } catch (PlayerMmrAlreadyActive e) {
            return Result.error(Lang.playerMmrAlreadyActive);
        } catch (playerHasAlreadyTimerStarted e) {
            return Result.error(Lang.playerChangeStatusAlreadyStarted.replace("{timeLeft}", e.formatTimerToString()));
        } catch (StatusUpdateCouldown e) {
            return Result.error(Lang.playerChangeStatusOnCooldown.replace("{cooldown}", e.formatTimerToString()));
        }
        String successMessage = disable ? Lang.playerMmrDisableSuccess : Lang.playerMmrEnableSuccess;
        return Result.ok(successMessage.replace(
                "{timer}",
                Config.PLAYER_CHANGE_STATUS_TIMER/60 + "")
        );
    }

}
