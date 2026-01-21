package com.atzer.inviteSMP.service;

import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.atzer.inviteSMP.database.repository.PluginPlayerRepository;
import com.atzer.inviteSMP.utils.PasswordUtils;
import com.atzer.inviteSMP.utils.PlayerInventorySerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

public final class PluginPlayerService {

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

    public boolean register(Player player, String identifier, Instant lastJoin, double money, String password) {
        try {
            return save(
                    identifier,
                    player.getUniqueId(),
                    player.displayName(),
                    lastJoin, money,
                    PlayerInventorySerializer.serialize(player.getInventory().getContents()),
                    PlayerInventorySerializer.serialize(player.getInventory().getArmorContents()),
                    PlayerInventorySerializer.serialize(player.getInventory().getExtraContents()),
                    password
            );
        } catch (IOException e) {
            this.logger.severe("It's not possible to serialize the inventory of the player " + player.getName() + " !");
            return false;
        }
    }

    public boolean save(String identifier, UUID uuid, Component name, Instant lastJoin, double money, String playerInventory, String armorInventory, String extraInventory, String password) {
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
                    armorInventory,
                    extraInventory,
                    hashedPassword
            ));
        } catch (SQLException e) {
            this.logger.severe("It's not possible to save the player " + name + " !");
            return false;
        }
        return true;
    }

    public Player toPlayer(PluginPlayer pluginPlayer) {
        Player player = Bukkit.getPlayer(pluginPlayer.uuid());

        if (player == null) {
            player = Bukkit.getOfflinePlayer(pluginPlayer.uuid()).getPlayer();

            if (player == null) {
                return null;
            }
        }

        player.displayName(pluginPlayer.name());
        player.getInventory().clear();
        try {
            player.getInventory().setContents(PlayerInventorySerializer.deserialize(pluginPlayer.playerInventory()));
            player.getInventory().setArmorContents(PlayerInventorySerializer.deserialize(pluginPlayer.armorInventory()));
            player.getInventory().setExtraContents(PlayerInventorySerializer.deserialize(pluginPlayer.extraInventory()));
        } catch (IOException | ClassNotFoundException e) {
            this.logger.severe("It's not possible to deserialize the inventory of the player " + player.getName() + " !");
            return null;
        }
        return player;
    }

    public void clearPlayer(Player player) {
        player.getInventory().clear();
    }
}
