package com.atzer.inviteSMP.service;

import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.atzer.inviteSMP.database.repository.PluginPlayerRepository;
import com.atzer.inviteSMP.utils.PasswordUtils;
import net.kyori.adventure.text.Component;
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
    public PluginPlayer loadFromUuid(UUID uuid) {
        PluginPlayer pluginPlayer = null;
        try {
            pluginPlayer = this.pluginPlayerRepository.findByUuid(uuid);
        } catch (SQLException e) {
            this.logger.severe("It's not possible to update the last join of the player with the uuid: " + uuid.toString() + " !");
        }
        return pluginPlayer;
    }

    public void save(String identifier, UUID uuid, Component name, Instant lastJoin, double money, String playerInventory, String password) {
        String hashedPassword = PasswordUtils.hashPassword(password);
        try {
            this.pluginPlayerRepository.create(new PluginPlayer(
                    0,
                    identifier,
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
}
