package fr.lordhydra.mmr;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
