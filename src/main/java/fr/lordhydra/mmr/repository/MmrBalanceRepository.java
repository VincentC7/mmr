package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.entities.MmrBalanceEntity;
import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class MmrBalanceRepository implements Repository {

    private static final String TABLE_NAME = "mmr_balance";

    @Override
    public void createTable() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                """
                (
                    first_player_uuid varchar(36) NOT NULL,
                    second_player_uuid varchar(36) NOT NULL,
                    created DATETIME NOT NULL,
                    updated DATETIME NOT NULL,
                    balance TINYINT NOT NULL,
                    PRIMARY KEY(first_player_uuid, second_player_uuid)
                );
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public void insert(MmrBalanceEntity mmrBalanceEntity) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = "INSERT INTO " + TABLE_NAME +
                """
                (
                    first_player_uuid,
                    second_player_uuid,
                    created,
                    updated,
                    balance
                ) VALUES (?, ?, ?, ?, ?);
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDateTime today = LocalDateTime.now();
            stmt.setString(1, mmrBalanceEntity.firstPlayerUUID().toString());
            stmt.setString(2, mmrBalanceEntity.secondPlayerUUID().toString());
            stmt.setString(3, today.toString());
            stmt.setString(4, today.toString());
            stmt.setInt(5, mmrBalanceEntity.balance());
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public MmrBalanceEntity findByPlayers(Player firstPlayer, Player secondPlayer) {
        Connection connection = StorageService.getInstance().getConnection();
        String request = "SELECT * FROM " + TABLE_NAME +
                " WHERE (first_player_uuid = ? AND second_player_uuid = ?) " +
                "OR (first_player_uuid = ? AND second_player_uuid = ?) ";
        try {
            PreparedStatement stmt = connection.prepareStatement(request);
            stmt.setString(1, firstPlayer.getUniqueId().toString());
            stmt.setString(2, secondPlayer.getUniqueId().toString());
            stmt.setString(3, secondPlayer.getUniqueId().toString());
            stmt.setString(4, firstPlayer.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return MmrBalanceEntity.builder()
                    .firstPlayerUUID(UUID.fromString(rs.getString(1)))
                    .secondPlayerUUID(UUID.fromString(rs.getString(2)))
                    .created(rs.getDate(3))
                    .updated(rs.getDate(4))
                    .balance(rs.getInt(5))
                    .build();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
            return null;
        }
    }

    public void update(MmrBalanceEntity mmrBalanceEntity) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = "UPDATE " + TABLE_NAME +
                     " SET updated = ?, balance = ? " +
                     "WHERE first_player_uuid = ? AND second_player_uuid = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDateTime today = LocalDateTime.now();
            stmt.setString(1, today.toString());
            stmt.setInt(2, mmrBalanceEntity.balance());
            stmt.setString(3, mmrBalanceEntity.firstPlayerUUID().toString());
            stmt.setString(4, mmrBalanceEntity.secondPlayerUUID().toString());
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

}
