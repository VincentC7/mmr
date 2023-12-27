package fr.lordhydra.mmr;

import fr.lordhydra.mmr.commands.MMRCommand;
import fr.lordhydra.mmr.commands.MmrAdminCommand;
import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.config.Lang;
import fr.lordhydra.mmr.listeners.PlayerDeathListener;
import fr.lordhydra.mmr.services.FileService;
import fr.lordhydra.mmr.services.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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

        //Commands
        Objects.requireNonNull(getCommand("mmr")).setExecutor(new MMRCommand());
        Objects.requireNonNull(getCommand("mmradmin")).setExecutor(new MmrAdminCommand());

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
