package com.atzer.inviteSMP.database.repository;

import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.text.minimessage.MiniMessage;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.UUID;

public class PluginPlayerRepository {

    private final HikariDataSource dataSource;

    public PluginPlayerRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public PluginPlayer findOrCreate(UUID uuid, String name) throws SQLException {
        PluginPlayer pluginPlayer = this.findByUuid(uuid);

        if (pluginPlayer == null) {
            this.create(uuid, name);
        }

        return pluginPlayer;
    }

    @Nullable
    public PluginPlayer findByUuid(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                SELECT * FROM players WHERE uuid = ?;
        """);

        preparedStatement.setString(1, uuid.toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new PluginPlayer(
                    resultSet.getInt("id"),
                    uuid,
                    MiniMessage.miniMessage().deserialize(resultSet.getString("name")),
                    resultSet.getTimestamp("first_join").toInstant(),
                    resultSet.getTimestamp("last_join").toInstant(),
                    resultSet.getDouble("money"),
                    resultSet.getString("player_inventory")
            );
        }

        resultSet.close();
        preparedStatement.close();
        return null;
    }

    public void create(UUID uuid, String name) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                INSERT INTO players  (uuid, name, first_join, last_join, money, player_inventory) VALUES (?, ?, NOW(), NOW(), ?, ?)
        """);

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, name);
        preparedStatement.setDouble(3, 0);
        preparedStatement.setString(4, null);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void create(PluginPlayer pluginPlayer) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                INSERT INTO players (uuid, name, first_join, last_join, money, player_inventory) VALUES (?, ?, ?, ?, ?, ?)
        """);

        preparedStatement.setString(1, pluginPlayer.uuid().toString());
        preparedStatement.setString(2, MiniMessage.miniMessage().serialize(pluginPlayer.name()));
        preparedStatement.setTimestamp(3, Timestamp.from(pluginPlayer.firstJoin()));
        preparedStatement.setTimestamp(4, Timestamp.from(pluginPlayer.lastJoin()));
        preparedStatement.setDouble(5, pluginPlayer.money());
        preparedStatement.setString(6, pluginPlayer.playerInventory());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteByUuid(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                DELETE  FROM players WHERE uuid = ?; 
        """);

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update(PluginPlayer pluginPlayer) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                UPDATE players SET name = ?, last_join = ?, money = ?, player_inventory = ? WHERE uuid = ?; 
        """);

        preparedStatement.setString(1, MiniMessage.miniMessage().serialize(pluginPlayer.name()));
        preparedStatement.setTimestamp(2, Timestamp.from(pluginPlayer.lastJoin()));
        preparedStatement.setDouble(3, pluginPlayer.money());
        preparedStatement.setString(4, pluginPlayer.playerInventory());
        preparedStatement.setString(5, pluginPlayer.uuid().toString());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void updateLastJoin(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                UPDATE players SET last_join = NOW() WHERE uuid = ?; 
        """);

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
