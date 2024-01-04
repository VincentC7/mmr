package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
