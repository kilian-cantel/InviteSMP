package com.atzer.inviteSMP.database.repository;

import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.text.minimessage.MiniMessage;

import javax.annotation.Nullable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PluginPlayerRepository {

    private final HikariDataSource dataSource;

    public PluginPlayerRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public PluginPlayer findOrCreate(int id, UUID uuid, String name) throws SQLException {
        PluginPlayer pluginPlayer = this.findById(id);

        if (pluginPlayer == null) {
            this.create(uuid, name);
        }

        return pluginPlayer;
    }

    @Nullable
    public PluginPlayer findById(int id) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                SELECT * FROM players WHERE id = ?;
        """);

        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new PluginPlayer(
                    id,
                    UUID.fromString(resultSet.getString("uuid")),
                    MiniMessage.miniMessage().deserialize(resultSet.getString("name")),
                    resultSet.getDate("first_join").toInstant(),
                    resultSet.getDate("last_join").toInstant(),
                    resultSet.getDouble("money"),
                    resultSet.getString("player_inventory")
            );
        }
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
                INSERT INTO players (uuid, name, first_join, last_join, money, player_inventory) VALUES (?, ?, ?, ?, ?)
        """);

        preparedStatement.setString(1, pluginPlayer.uuid().toString());
        preparedStatement.setString(2, MiniMessage.miniMessage().serialize(pluginPlayer.name()));
        preparedStatement.setDate(3, new Date(pluginPlayer.firstJoin().toEpochMilli()));
        preparedStatement.setDate(4, new Date(pluginPlayer.lastJoin().toEpochMilli()));
        preparedStatement.setDouble(5, pluginPlayer.money());
        preparedStatement.setString(6, pluginPlayer.playerInventory());
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    public void deleteById(int id) throws SQLException {
        PreparedStatement preparedStatement = this.dataSource.getConnection().prepareStatement("""
                DELETE  FROM players WHERE id = ?; 
        """);

        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }
}
