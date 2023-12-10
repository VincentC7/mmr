package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class PlayerMmrRepository {

    public static boolean createTable() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                CREATE TABLE IF NOT EXISTS PlayerMmr(
                    id INT(10) not null auto_increment,
                    playerUUID varchar(36) NOT NULL,
                    created DATETIME NOT NULL,
                    updated DATETIME NOT NULL,
                    mmr Float(8,2) NOT NULL,
                    PRIMARY KEY(id)
                );
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return false;
    }

    public PlayerMmrEntity findByPlayer(Player player) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                SELECT *
                FROM PlayerMmr
                WHERE playerUUID = ?
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return PlayerMmrEntity.builder()
                        .id(rs.getInt(1))
                        .playerUUID(UUID.fromString(rs.getString(2)))
                        .created(rs.getString(3))
                        .updated(rs.getString(4))
                        .mmr(rs.getDouble(5))
                        .build();
            }
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return null;
    }

    public void insert(Player player) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                INSERT INTO PlayerMmr(playerUUID, created, updated, mmr) VALUES (?, ?, ?, ?);
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDate today = LocalDate.now();
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setDate(2, Date.valueOf(today));
            stmt.setDate(3, Date.valueOf(today));
            stmt.setDouble(4, Config.DEFAULT_MMR);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public void updatePlayerMmr(Player player, double newMmr) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                 Update PlayerMmr SET mmr = ? and updated = ? WHERE playerUUID = ?;
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDate today = LocalDate.now();
            stmt.setDouble(1, newMmr);
            stmt.setDate(2, Date.valueOf(today));
            stmt.setString(3, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }
}
