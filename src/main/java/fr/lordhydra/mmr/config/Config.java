package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static void load() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("config").getConfig();
    }
}
