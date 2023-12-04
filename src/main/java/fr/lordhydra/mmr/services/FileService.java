package fr.lordhydra.mmr.services;


import fr.lordhydra.mmr.utils.Logger;
import fr.lordhydra.mmr.utils.ConfigFile;

import java.util.HashMap;

public class FileService {

    private final HashMap<String, ConfigFile> files;

    private static FileService instance;

    public static FileService getInstance() {
        if (instance == null) instance = new FileService();
        return instance;
    }

    private FileService() {
        files = new HashMap<>();
        files.put("config", new ConfigFile("config"));
        files.put("lang", new ConfigFile("lang"));
        files.put("database", new ConfigFile("database"));
    }

    public void launchFiles() {
        files.forEach((key, file) -> {
            file.init();
        });
    }

    public void reload() {
        files.forEach((key, file) -> {
            file.reload();
        });
    }

    public ConfigFile getFile(String name) {
        if (!files.containsKey(name)){
            Logger.getInstance().error("Fichier inconnu");
            return null;
        }
        return files.get(name);
    }

}
