package fr.lordhydra.mmr.repository;

import fr.lordhydra.mmr.config.Config;
import fr.lordhydra.mmr.entities.PlayerMmrEntity;
import fr.lordhydra.mmr.services.StorageService;
import fr.lordhydra.mmr.utils.DateUtil;
import fr.lordhydra.mmr.utils.Logger;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerMmrRepository implements Repository{

    private static final String TABLE_NAME = "player_mmr";

    @Override
    public void createTable() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                """
                (
                    id INT(10) not null auto_increment,
                    player_uuid varchar(36) NOT NULL,
                    player_name varchar(36) NOT NULL,
                    created DATETIME NOT NULL,
                    mmr_updated DATETIME NOT NULL,
                    mmr DECIMAL(8,2) NOT NULL,
                    is_active BOOLEAN DEFAULT true,
                    status_updated DATETIME NOT NULL,
                    PRIMARY KEY(id)
                );
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public PlayerMmrEntity findByPlayer(Player player) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE player_uuid = ?;";
        return findByField(sql, String.valueOf(player.getUniqueId()));
    }

    public PlayerMmrEntity findByPlayerName(String playerName) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE player_name = ?;";
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
                        .id(rs.getInt("id"))
                        .playerUUID(UUID.fromString(rs.getString("player_uuid")))
                        .playerName(rs.getString("player_name"))
                        .created(DateUtil.parseDateFromDb(rs.getString("created")))
                        .mmrUpdated(DateUtil.parseDateFromDb(rs.getString("mmr_updated")))
                        .mmr(rs.getBigDecimal("mmr"))
                        .isActive(rs.getBoolean("is_active"))
                        .statusUpdated(DateUtil.parseDateFromDb(rs.getString("status_updated")))
                        .build();
            }
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return null;
    }

    public void insertPlayerMmr(PlayerMmrEntity playerMmrEntity) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql =
                "INSERT INTO "+ TABLE_NAME +
                        """
                        (
                            player_uuid,
                            player_name,
                            created,
                            mmr_updated,
                            mmr,
                            status_updated
                        ) VALUES (?, ?, ?, ?, ?, ?);
                        """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            LocalDateTime today = LocalDateTime.now();
            stmt.setString(1, playerMmrEntity.playerUUID().toString());
            stmt.setString(2, playerMmrEntity.playerName());
            stmt.setString(3, today.toString());
            stmt.setString(4, today.toString());
            stmt.setBigDecimal(5, playerMmrEntity.mmr());
            stmt.setString(6, today.toString());
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    public boolean updatePlayerMmr(PlayerMmrEntity playerMmrEntity) {
        Connection connection = StorageService.getInstance().getConnection();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sql = "Update "+ TABLE_NAME + " " +
                """
                SET mmr = ?,
                    mmr_updated = ?,
                    is_active = ?,
                    status_updated = ?
                WHERE player_uuid = ?;
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, playerMmrEntity.mmr());
            stmt.setString(2, playerMmrEntity.mmrUpdated().toString());
            stmt.setBoolean(3, playerMmrEntity.isActive());
            stmt.setString(4,playerMmrEntity.statusUpdated().toString());
            stmt.setString(5, playerMmrEntity.playerUUID().toString());
            Logger.getInstance().info(stmt.toString());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
            return false;
        }
    }

    public ArrayList<PlayerMmrEntity> getPlayersMmrs(int page) {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = "SELECT * FROM "+TABLE_NAME+" order by mmr desc LIMIT ? OFFSET ?;";
        ArrayList<PlayerMmrEntity> playerMmrEntities = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Config.TOP_MMR_PAGE_SIZE);
            stmt.setInt(2, Config.TOP_MMR_PAGE_SIZE * (page - 1));
            ResultSet rs = stmt.executeQuery();
            Logger.getInstance().info(stmt.toString());
            while (rs.next()) {
                PlayerMmrEntity playerMmrEntity = PlayerMmrEntity.builder()
                        .playerUUID(UUID.fromString(rs.getString("player_uuid")))
                        .playerName(rs.getString("player_name"))
                        .created(DateUtil.parseDateFromDb(rs.getString("created")))
                        .mmrUpdated(DateUtil.parseDateFromDb(rs.getString("mmr_updated")))
                        .mmr(rs.getBigDecimal("mmr"))
                        .isActive(rs.getBoolean("is_active"))
                        .statusUpdated(DateUtil.parseDateFromDb(rs.getString("status_updated")))
                        .build();
                playerMmrEntities.add(playerMmrEntity);
            }
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return playerMmrEntities;
    }

    public int countPlayerMmr() {
        Connection connection = StorageService.getInstance().getConnection();
        String sql = "SELECT count(*) as count FROM "+ TABLE_NAME +";";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Logger.getInstance().info(stmt.toString());
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            Logger.getInstance().error(e.getMessage());
        }
        return 0;
    }
}
