package com.atzer.inviteSMP.command;

import com.atzer.inviteSMP.InviteSMP;
import com.atzer.inviteSMP.NamespacedKeys;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConnectCommand implements TabExecutor {

    private static final InviteSMP plugin = InviteSMP.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You must be a player to execute this command!"));
            return false;
        }

            if (Boolean.TRUE.equals(player.getPersistentDataContainer().get(NamespacedKeys.CONNECTED.getNamespacedKey(), PersistentDataType.BOOLEAN))) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are already connected!"));
            return false;
        }

        player.getPersistentDataContainer().set(NamespacedKeys.CONNECTING.getNamespacedKey(), PersistentDataType.BOOLEAN, true);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
