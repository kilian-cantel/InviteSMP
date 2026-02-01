package com.atzer.inviteSMP.database.model;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.time.Instant;
import java.util.UUID;

public record PluginPlayer (
        int id,
        String identifier,
        UUID uuid,
        Component name,
        Instant firstJoin,
        Instant lastJoin,
        double money,
        String playerInventory,
        String armorInventory,
        String extraInventory,
        String password,
        Location location
){}