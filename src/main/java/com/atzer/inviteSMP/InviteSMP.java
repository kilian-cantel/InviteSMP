package com.atzer.inviteSMP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class InviteSMP extends JavaPlugin {

    private static JavaPlugin instance;
    private FileConfiguration baseConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.baseConfig = this.getConfig();

        this.getLogger().info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        instance = null;

        this.getLogger().info("Plugin has been disabled!");
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public FileConfiguration getBaseConfig() {
        return this.baseConfig;
    }

    public void reload() {
        this.reloadConfig();
        this.baseConfig = this.getConfig();
    }
}
