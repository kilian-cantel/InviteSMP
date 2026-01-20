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
    public PluginPlayer findByUuid(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                SELECT * FROM players WHERE uuid = ?;
        """);

        preparedStatement.setString(1, uuid.toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new PluginPlayer(
                    resultSet.getInt("id"),
                    resultSet.getString("identifier"),
                    uuid,
                    MiniMessage.miniMessage().deserialize(resultSet.getString("name")),
                    resultSet.getTimestamp("first_join").toInstant(),
                    resultSet.getTimestamp("last_join").toInstant(),
                    resultSet.getDouble("money"),
                    resultSet.getString("player_inventory"),
                    resultSet.getString("password")
            );
        }

        resultSet.close();
        preparedStatement.close();
        return null;
    }

    public void create(PluginPlayer pluginPlayer) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                INSERT INTO players (uuid, identifier, name, first_join, last_join, money, player_inventory, password) VALUES (?, ?, ?, ?, ?, ?, ?, )
        """);

        preparedStatement.setString(1, pluginPlayer.uuid().toString());
        preparedStatement.setString(2, pluginPlayer.identifier());
        preparedStatement.setString(3, MiniMessage.miniMessage().serialize(pluginPlayer.name()));
        preparedStatement.setTimestamp(4, Timestamp.from(pluginPlayer.firstJoin()));
        preparedStatement.setTimestamp(5, Timestamp.from(pluginPlayer.lastJoin()));
        preparedStatement.setDouble(6, pluginPlayer.money());
        preparedStatement.setString(7, pluginPlayer.playerInventory());
        preparedStatement.setString(8, pluginPlayer.password());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteByIdentifier(String identifier) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                DELETE  FROM players WHERE identifier = ?;
        """);

        preparedStatement.setString(1, identifier);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update(String identifier, PluginPlayer pluginPlayer) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                UPDATE players SET uuid = ?, name = ?, last_join = ?, money = ?, player_inventory = ?, identifier = ? WHERE identifier = ?;
        """);

        preparedStatement.setString(1, pluginPlayer.uuid().toString());
        preparedStatement.setString(2, MiniMessage.miniMessage().serialize(pluginPlayer.name()));
        preparedStatement.setTimestamp(3, Timestamp.from(pluginPlayer.lastJoin()));
        preparedStatement.setDouble(4, pluginPlayer.money());
        preparedStatement.setString(5, pluginPlayer.playerInventory());
        preparedStatement.setString(6, pluginPlayer.identifier());
        preparedStatement.setString(7, identifier);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
