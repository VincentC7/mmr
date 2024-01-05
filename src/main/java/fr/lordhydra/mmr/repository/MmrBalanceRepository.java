package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.entities.MmrBalanceEntity;
import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MmrBalanceRepository implements Repository {

    @Override
    public void createTable() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                CREATE TABLE IF NOT EXISTS MmrBalance(
                    first_player_uuid varchar(36) NOT NULL,
                    second_player_uuid varchar(36) NOT NULL,
                    created DATETIME NOT NULL,
                    updated DATETIME NOT NULL,
                    first_player_balance TINYINT NOT NULL,
                    second_player_balance TINYINT NOT NULL,
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
        String sql = """
                INSERT INTO MmrBalance(
                    first_player_uuid,
                    second_player_uuid,
                    created,
                    updated,
                    first_player_balance,
                    second_player_balance
                ) VALUES (?, ?, ?, ?, ?, ?);
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDateTime today = LocalDateTime.now();
            Logger.getInstance().info(today.toString());
            stmt.setString(1, mmrBalanceEntity.firstPlayerUUID().toString());
            stmt.setString(2, mmrBalanceEntity.secondPlayerUUID().toString());
            stmt.setString(3, today.toString());
            stmt.setString(4, today.toString());
            stmt.setInt(5, 0);
            stmt.setInt(6, 0);
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

}
