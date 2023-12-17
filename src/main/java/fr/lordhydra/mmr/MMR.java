package fr.lordhydra.mmr;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.listeners.PlayerDeathListener;
import fr.lordhydra.mmr.services.FileService;
import fr.lordhydra.mmr.services.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MMR extends JavaPlugin {

    @Getter
    @Setter
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        setInstance(this);
        getLogger().info("started");

        //Fichiers de config
        FileService.getInstance().launchFiles();
        Lang.load();
        Config.load();

        //Services
        StorageService.getInstance().init();

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
