package fr.lordhydra.mmr.utils;

import fr.lordhydra.mmr.MMR;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Logger {

    private static Logger instance;
    private static JavaPlugin main;

    private Logger() {
        main = MMR.getInstance();
    }

    public static Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    public void info(String message) {
        main.getLogger().log(Level.INFO, message);
    }

    public void error(String error) {
        main.getLogger().log(Level.SEVERE, error);
    }

    public void warning(String error) {
        main.getLogger().log(Level.WARNING, error);
    }
}
