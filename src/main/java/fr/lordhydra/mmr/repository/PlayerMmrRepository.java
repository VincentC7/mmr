package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
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
                        .created(rs.getString(4))
                        .updated(rs.getString(5))
                        .mmr(rs.getDouble(6))
                        .build();
            }
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return null;
    }

    public void insertPlayerMmr(Player player, BigDecimal newMmr) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                INSERT INTO PlayerMmr(playerUUID, playerName, created, updated, mmr) VALUES (?, ?, ?, ?, ?);
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDate today = LocalDate.now();
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setDate(3, Date.valueOf(today));
            stmt.setDate(4, Date.valueOf(today));
            stmt.setBigDecimal(5, newMmr);
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public void updatePlayerMmr(Player player, BigDecimal newMmr) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                 Update PlayerMmr SET mmr = ? WHERE playerUUID = ?;
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, newMmr);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
            Logger.getInstance().info(stmt.toString());
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }
}
