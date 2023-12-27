package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerMmrRepository {

    public static boolean createTable() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                CREATE TABLE IF NOT EXISTS PlayerMmr(
                    id INT(10) not null auto_increment,
                    playerUUID varchar(36) NOT NULL,
                    playerName varchar(36) NOT NULL,
                    created DATETIME NOT NULL,
                    updated DATETIME NOT NULL,
                    mmr DECIMAL(8,2) NOT NULL,
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
        String sql = """
                SELECT *
                FROM PlayerMmr
                WHERE playerUUID = ?
                """;
        return findByField(sql, String.valueOf(player.getUniqueId()));
    }

    public PlayerMmrEntity findByPlayerName(String playerName) {
        String sql = """
                SELECT *
                FROM PlayerMmr
                WHERE playerName = ?
                """;
        return findByField(sql, playerName);
    }

    private PlayerMmrEntity findByField(String sql, String field) {
        Connection connection = StorageService.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, field);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return PlayerMmrEntity.builder()
                        .id(rs.getInt(1))
                        .playerUUID(UUID.fromString(rs.getString(2)))
                        .playerName(rs.getString(3))
                        .created(rs.getDate(4))
                        .updated(rs.getDate(5))
                        .mmr(rs.getBigDecimal(6))
                        .build();
            }
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return null;
    }

    public void insertPlayerMmr(PlayerMmrEntity playerMmrEntity) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                INSERT INTO PlayerMmr(playerUUID, playerName, created, updated, mmr) VALUES (?, ?, ?, ?, ?);
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDateTime today = LocalDateTime.now();
            Logger.getInstance().info(today.toString());
            stmt.setString(1, playerMmrEntity.playerUUID().toString());
            stmt.setString(2, playerMmrEntity.playerName());
            stmt.setString(3, today.toString());
            stmt.setString(4, today.toString());
            stmt.setBigDecimal(5, playerMmrEntity.mmr());
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public boolean updatePlayerMmr(PlayerMmrEntity playerMmrEntity) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                 Update PlayerMmr
                    SET mmr = ?, updated = ?
                    WHERE playerUUID = ?;
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, playerMmrEntity.mmr());
            stmt.setString(2, LocalDateTime.now().toString());
            stmt.setString(3, playerMmrEntity.playerUUID().toString());
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
            return false;
        }
    }
}
