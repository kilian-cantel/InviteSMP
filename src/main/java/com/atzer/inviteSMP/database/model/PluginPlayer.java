package com.atzer.inviteSMP.database.model;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.PlayerInventory;

import java.time.Instant;
import java.util.UUID;

public record PluginPlayer (
        int id,
        UUID uuid,
        Component name,
        Instant firstJoin,
        Instant lastJoin,
        double money,
        String playerInventory
){}