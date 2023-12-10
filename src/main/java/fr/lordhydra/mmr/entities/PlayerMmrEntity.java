package fr.lordhydra.mmr.entities;

import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.Logger;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Builder
@ToString
@Accessors(fluent = true)
public class PlayerMmrEntity {

    private int id;

    @Getter
    private UUID player;

    @Getter
    private String created, updated;

    public static boolean createTable() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = """
                CREATE TABLE IF NOT EXISTS PlayerMmr(
                    id INT(10) not null auto_increment,
                    playerUUID varchar(36),
                    created DATETIME,
                    updated DATETIME,
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





}
