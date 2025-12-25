package com.atzer.inviteSMP.database;

import com.atzer.inviteSMP.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private HikariDataSource dataSource;

    public void init() throws IOException, SQLException {
        if (this.dataSource != null) {
            this.dataSource.close();
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        hikariConfig.setJdbcUrl(
                "jdbc:mariadb://" +
                        Config.DATABASE_HOST.getString() + ":" +
                        Config.DATABASE_PORT.getInt() + "/" +
                        Config.DATABASE_NAME.getString()
        );
        hikariConfig.setUsername(Config.DATABASE_USER.getString());
        hikariConfig.setPassword(Config.DATABASE_PASSWORD.getString());

        hikariConfig.setMaximumPoolSize(Config.DATABASE_POOL_MAX_POOL_SIZE.getInt());
        hikariConfig.setMinimumIdle(Config.DATABASE_POOL_MINIMUM_IDLE.getInt());
        hikariConfig.setMaxLifetime(Config.DATABASE_POOL_MAX_LIFETIME.getInt());
        hikariConfig.setConnectionTimeout(Config.DATABASE_POOL_CONNECTION_TIMEOUT.getInt());
        hikariConfig.setPoolName("InviteSMP");

        this.dataSource = new HikariDataSource(hikariConfig);

        createTables(this.dataSource.getConnection());
    }

    public HikariDataSource getDataSource() {
        return this.dataSource;
    }

    public void shutdown() {
        if (this.dataSource != null) {
            this.dataSource.close();
            this.dataSource = null;
        }
    }

    public void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS players (
                id INT AUTO_INCREMENT PRIMARY KEY,
                uuid TEXT NOT NULL UNIQUE,
                name TEXT NOT NULL,
                first_join TIMESTAMP NULL DEFAULT NULL,
                last_join TIMESTAMP NULL DEFAULT NULL,
                money DOUBLE NOT NULL DEFAULT 0,
                helmet TEXT NOT NULL DEFAULT 0,
                chestplate TEXT NOT NULL DEFAULT 0,
                legging TEXT NOT NULL DEFAULT 0,
                boots TEXT NOT NULL DEFAULT 0,
                inventory TEXT NOT NULL DEFAULT 0,
        """);

        statement.close();
    }

}
