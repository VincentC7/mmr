package fr.lordhydra.mmr.commands;

import fr.lordhydra.mmr.error.Result;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public abstract class AbstractCommand implements CommandExecutor {

    public static final String PLUGIN_PREFIX =
            GRAY + "[" + GOLD + "MMR"+ GRAY + "]: ";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player player) executePlayerCommand(player, args);
        if (commandSender instanceof ConsoleCommandSender) executeTerminalCommand(args);
        return true;
    }

    private void executePlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            displayHelp(player);
        } else {
            Result result = mapPlayerAction(player, args[0], Arrays.copyOfRange(args, 1, args.length));
            if (result.hasMessage()) {
                String message = PLUGIN_PREFIX +
                        (result.isOk() ? GREEN : RED) +
                        result.getMessage();
                player.sendMessage(message);
            }
        }
    }

    private void executeTerminalCommand(String[] args) {
        if (args.length == 0) {
            return;
        }
        Result result = mapTerminalAction(args[0], Arrays.copyOfRange(args, 1, args.length));
        if (result.hasMessage()) {
            if (result.isOk()){
                Logger.getInstance().info(result.getMessage());
            } else {
                Logger.getInstance().error(result.getMessage());
            }
        }
    }

    protected abstract Result mapPlayerAction(Player player, String action, String[] args);
    protected abstract Result mapTerminalAction(String action, String[] args);
    protected abstract void displayHelp(Player player);

}