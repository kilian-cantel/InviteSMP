package com.atzer.inviteSMP.service;

import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.atzer.inviteSMP.database.repository.PluginPlayerRepository;
import com.atzer.inviteSMP.utils.PasswordUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.time.Instant;
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
    public PluginPlayer load(String name, UUID uuid) {
        try {
            return this.pluginPlayerRepository.findOrCreate(uuid, name);
        } catch (SQLException e) {
            this.logger.severe("It's not possible to load the player " + name + " !");
        }
        return null;
    }

    public void save(UUID uuid, Component name, Instant lastJoin, double money, String playerInventory, String password) {
        String hashedPassword = PasswordUtils.hashPassword(password);
        try {
            this.pluginPlayerRepository.create(new PluginPlayer(
                    0,
                    uuid,
                    name,
                    lastJoin,
                    lastJoin,
                    money,
                    playerInventory,
                    hashedPassword
            ));
        } catch (SQLException e) {
            this.logger.severe("It's not possible to save the player " + name + " !");
        }
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
