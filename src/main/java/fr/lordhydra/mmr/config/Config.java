package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static Double ON_DEATH_RATE, ON_KILL_RATE;

    public static int DEFAULT_MMR, TOP_MMR_PAGE_SIZE;

    public static void load() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("config").getConfig();
        ON_DEATH_RATE = configuration.getDouble("onDeathRate");
        ON_KILL_RATE = configuration.getDouble("onKillRate");
        DEFAULT_MMR = configuration.getInt("defaultMmr");
        TOP_MMR_PAGE_SIZE = configuration.getInt("topMmrPageSize");
    }
}
