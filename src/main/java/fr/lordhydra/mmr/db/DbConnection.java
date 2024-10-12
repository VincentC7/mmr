package fr.lordhydra.mmr.db;

import fr.lordhydra.mmr.MMR;
import fr.lordhydra.mmr.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private final DbCredentials dbCredentials;

    @Getter
    private Connection connection;
    private BukkitTask autoRestartTask;

    public DbConnection() {
        dbCredentials = new DbCredentials();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(
                    dbCredentials.toUri(),
                    dbCredentials.getDbUser(),
                    dbCredentials.getDbPass()
            );
            if (autoRestartTask == null || autoRestartTask.isCancelled()) {
                enableAutoRestart();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection!=null && !connection.isClosed()){
                autoRestartTask.cancel();
                connection.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connect();
    }

    public void enableAutoRestart() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Logger.getInstance().info("Db connection restarted");
        autoRestartTask = scheduler.runTaskTimer(MMR.getInstance(), this::restart, 20L * 3600L, 20L * 3600L);
    }

}
