package fr.lordhydra.mmr.services;

import fr.lordhydra.mmr.db.DbConnection;
import fr.lordhydra.mmr.repository.MmrBalanceRepository;
import fr.lordhydra.mmr.repository.PlayerMmrRepository;

import java.sql.Connection;

public class StorageService {

    private static StorageService instance;

    private final DbConnection dbConnection;

    private StorageService() {
        dbConnection = new DbConnection();
    }

    public static StorageService getInstance() {
        if (instance == null) instance = new StorageService();
        return instance;
    }

    public void init() {
        dbConnection.connect();
        new PlayerMmrRepository().createTable();
        new MmrBalanceRepository().createTable();
    }

    public void stop() {
        dbConnection.close();
    }

    public Connection getConnection(){
        return dbConnection.getConnection();
    }
}
