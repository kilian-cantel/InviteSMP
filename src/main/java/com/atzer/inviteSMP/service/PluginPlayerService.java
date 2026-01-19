package com.atzer.inviteSMP.service;

import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.atzer.inviteSMP.database.repository.PluginPlayerRepository;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class PluginPlayerService {

    private final PluginPlayerRepository pluginPlayerRepository;
    private final Logger logger;

    public PluginPlayerService(PluginPlayerRepository pluginPlayerRepository, Logger logger) {
        this.pluginPlayerRepository = pluginPlayerRepository;
        this.logger = logger;
    }

    @Nullable
    public PluginPlayer loadAfterJoin(Player player) {
        return this.loadAfterJoin(player.getName(), player.getUniqueId());
    }

    @Nullable
    public PluginPlayer loadAfterJoin(OfflinePlayer player) {
        return this.loadAfterJoin(player.getName(), player.getUniqueId());
    }

    @Nullable
    public PluginPlayer loadAfterJoin(String name, UUID uuid) {
        try {
            this.pluginPlayerRepository.updateLastJoin(uuid);
        } catch (SQLException e) {
            this.logger.severe("It's not possible to update the last join of the player " + name + " !");
            return null;
        }

        return this.load(name, uuid);
    }

    @Nullable
    public PluginPlayer load(Player player) {
        return this.load(player.getName(), player.getUniqueId());
    }

    @Nullable
    public PluginPlayer load(OfflinePlayer player) {
        return this.load(player.getName(), player.getUniqueId());
    }

    @Nullable
    public PluginPlayer load(String name, UUID uuid) {
        try {
            return this.pluginPlayerRepository.findOrCreate(uuid, name);
        } catch (SQLException e) {
            this.logger.severe("It's not possible to load the player " + name + " !");
        }
        return null;
    }

    public PluginPlayer reload(UUID uuid) {
        try {
            return this.pluginPlayerRepository.findByUuid(uuid);
        } catch (SQLException e) {
            this.logger.severe("It's not possible to reload the player " + uuid + " !");
        }
        return null;
    }

}
