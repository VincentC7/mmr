package fr.lordhydra.mmr.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
@AllArgsConstructor
public class HelpCommand {
    private String command, description;

    public String buildHelpDescription() {
        String helpCommandColored = command
                .replaceAll("<", ChatColor.RED + "<")
                .replaceAll(">",  ">" + ChatColor.AQUA);
        return " - " +
                ChatColor.AQUA +
                helpCommandColored +
                ChatColor.YELLOW +
                " : " +
                description;
    }
}
