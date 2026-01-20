package com.atzer.inviteSMP;

import com.atzer.inviteSMP.database.DatabaseManager;
import com.atzer.inviteSMP.database.repository.PluginPlayerRepository;
import com.atzer.inviteSMP.listener.PlayerJoinListener;
import com.atzer.inviteSMP.service.PluginPlayerService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public final class InviteSMP extends JavaPlugin {

    private static InviteSMP instance;
    private FileConfiguration baseConfig;
    private DatabaseManager databaseManager;
    private PluginPlayerService pluginPlayerService;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.baseConfig = this.getConfig();

        //Load database
        this.getLogger().info("Loading database...");
        this.databaseManager = new DatabaseManager();
        try {
            this.databaseManager.init();
        } catch (IOException | SQLException e) {
            this.getLogger().severe("It's not possible to connect to the database !" + e.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.pluginPlayerService = new PluginPlayerService(
                new PluginPlayerRepository(this.databaseManager.getDataSource()),
                this.getLogger()
        );

        this.getLogger().info("Database has been loaded!");

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        this.getLogger().info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        instance = null;

        this.getLogger().info("Plugin has been disabled!");
    }

    public static InviteSMP getInstance() {
        return instance;
    }

    public FileConfiguration getBaseConfig() {
        return this.baseConfig;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public PluginPlayerService getPlayerService() {
        return this.pluginPlayerService;
    }

    public void reload() {
        this.reloadConfig();
        this.baseConfig = this.getConfig();
    }
}
