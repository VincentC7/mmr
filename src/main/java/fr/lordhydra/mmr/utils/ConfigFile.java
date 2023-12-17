package fr.lordhydra.mmr.utils;

import fr.lordhydra.mmr.MMR;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

	private File file;
	private final String fileName;
	protected FileConfiguration config;

	private final JavaPlugin main;

	public ConfigFile(String fileName) {
		main = MMR.getInstance();
		this.fileName = fileName + ".yml";
	}

	public void reload() {
		try {
			file = new File(main.getDataFolder(), this.fileName);
			config.load(getConfigFile());
		} catch (IOException | InvalidConfigurationException e) {
			Logger.getInstance().error(e.toString());
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			config.save(getConfigFile());
		} catch (IOException e) {
			Logger.getInstance().error(e.toString());
			e.printStackTrace();
		}
	}
	
	//function to update config on new version
	public void setConfig(String path, Object value) {
		if(!this.getConfig().isSet(path)){
			getConfig().set(path, value);
			Logger.getInstance().info("Added config option '"+path+"' in config.yml");
			try {
				getConfig().save(getConfigFile());
				getConfig().load(getConfigFile());
			} catch (IOException | InvalidConfigurationException e) {
				Logger.getInstance().error(e.toString());
				e.printStackTrace();
			}
		}
	}

	public void init() {
		file = new File(main.getDataFolder(), fileName);
	    if (!file.exists()) {
			Logger.getInstance().info("Création de "+ fileName);
	        file.getParentFile().mkdirs();
	        main.saveResource( fileName, false);
	    }
	    config = new YamlConfiguration();
	    try {
	        config.load(file);
	    } catch (IOException | InvalidConfigurationException e) {
			Logger.getInstance().error(e.toString());
	        e.printStackTrace();
	    }
	}

	private File getConfigFile() {
		return file;
	}

	public FileConfiguration getConfig() {
		return config;
	}
}
