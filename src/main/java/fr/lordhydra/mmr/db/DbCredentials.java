package fr.lordhydra.mmr.db;

import fr.lordhydra.mmr.services.FileService;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class DbCredentials {

    @Getter
    private final String dbJdbc, dbHost, dbUser, dbPass, dbName;
    private final int dbPort;

    @Getter
    private final boolean dbEnabled;

    public DbCredentials() {
        FileConfiguration config = FileService.getInstance()
                .getFile("database").getConfig();
        dbEnabled = config.getBoolean("dbEnabled");
        dbHost = config.getString("host");
        dbJdbc = config.getString("jdbc");
        dbUser = config.getString("user");
        dbPass = config.getString("pass");
        dbName = config.getString("name");
        dbPort = config.getInt("port");
    }

    public String toUri() {
        return "jdbc:"+dbJdbc+"://"+dbHost+":"+dbPort+"/"+dbName;
    }

}
