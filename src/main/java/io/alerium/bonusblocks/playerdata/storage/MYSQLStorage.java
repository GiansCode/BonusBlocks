package io.alerium.bonusblocks.playerdata.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.alerium.bonusblocks.BonusBlocksPlugin;
import io.alerium.bonusblocks.playerdata.PlayerData;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MYSQLStorage implements Storage {
    
    private final BonusBlocksPlugin plugin = BonusBlocksPlugin.getInstance();

    private final String hostname;
    private final String username;
    private final String password;
    private final String database;
    private final int port;
    
    private HikariDataSource dataSource;
    
    public MYSQLStorage() {
        ConfigurationSection section = plugin.getConfiguration().getConfig().getConfigurationSection("storage.mysql");
        
        hostname = section.getString("hostname");
        username = section.getString("username");
        password = section.getString("password");
        database = section.getString("database");
        port = section.getInt("port");
    }
    
    @Override
    public void setup() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("PrisonBestPool");

        dataSource = new HikariDataSource(config);
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata (uuid VARCHAR(36) PRIMARY KEY NOT NULL, broken_blocks INTEGER NOT NULL);")
        ) {
            
            statement.execute();
        }
    }

    @Override
    public PlayerData loadData(UUID uuid) throws Exception {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerdata WHERE uuid = ?;");
        ) {
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                int blocks = result.getInt("broken_blocks");
                result.close();
                return new PlayerData(uuid, blocks);
            } 
            
            result.close();
            return createData(uuid);
        }
    }

    @Override
    public void saveData(PlayerData playerData) throws Exception {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE playerdata SET broken_blocks = ? WHERE uuid = ?;");
        ) {
            
            statement.setInt(1, playerData.getBrokenBlocks());
            statement.setString(2, playerData.getUuid().toString());
        }
    }

    @Override
    public List<UUID> getTop() throws Exception {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerdata ORDER BY broken_blocks DESC LIMIT 10;");
                ResultSet result = statement.executeQuery();
        ) {

            List<UUID> top = new ArrayList<>();
            while (result.next())
                top.add(UUID.fromString(result.getString("uuid")));

            return top;
        }
    }

    private PlayerData createData(UUID uuid) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO playerdata (uuid, broken_blocks) VALUES (?, ?);");
        ) {
            
            statement.setString(1, uuid.toString());
            statement.setInt(2, 0);
            statement.execute();
        }
        
        return new PlayerData(uuid, 0);
    }

}
