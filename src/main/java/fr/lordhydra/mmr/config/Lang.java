package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.file.FileConfiguration;

public class Lang {

    //================= Commands ================= //

    //Default
    public static String unknownCommand, cantBeExecuteByTerminal;

    //================= Commands ================= //

    public static void load() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("lang").getConfig();
        unknownCommand = configuration.getString("commands.default.unknownCommand");
        cantBeExecuteByTerminal = configuration.getString("commands.default.cantBeExecuteByTerminal");
    }
}
