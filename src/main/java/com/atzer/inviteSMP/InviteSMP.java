package com.atzer.inviteSMP;

import com.atzer.inviteSMP.command.ConnectCommand;
import com.atzer.inviteSMP.database.DatabaseManager;
import com.atzer.inviteSMP.database.repository.PluginPlayerRepository;
import com.atzer.inviteSMP.listener.PlayerChatListener;
import com.atzer.inviteSMP.listener.PlayerJoinListener;
import com.atzer.inviteSMP.listener.PlayerMoveListener;
import com.atzer.inviteSMP.service.PluginPlayerService;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public final class InviteSMP extends JavaPlugin {

    private static InviteSMP instance;
    private DatabaseManager databaseManager;
    private PluginPlayerService pluginPlayerService;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        //Load database
        this.getLogger().info("Loading database...");
        this.databaseManager = new DatabaseManager(this);
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

        //Events
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        this.getLogger().info("Events have been registered!");

        //Commands
        PluginCommand connectCommand = this.getCommand("connect");

        if (connectCommand == null) {
            this.getLogger().severe("It's not possible to register the command connect !");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        connectCommand.setExecutor(new ConnectCommand());
        this.getLogger().info("Command connect has been registered!");

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

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public PluginPlayerService getPlayerService() {
        return this.pluginPlayerService;
    }

    public void reload() {
        this.reloadConfig();
    }
}
