package com.atzer.inviteSMP.listener;

import com.atzer.inviteSMP.Config;
import com.atzer.inviteSMP.InviteSMP;
import com.atzer.inviteSMP.NamespacedKeys;
import com.atzer.inviteSMP.database.model.PluginPlayer;
import com.atzer.inviteSMP.service.PluginPlayerService;
import com.atzer.inviteSMP.utils.VoidGenerator;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;

public final class PlayerJoinListener implements Listener {

    private static final InviteSMP plugin = InviteSMP.getInstance();
    private static final PluginPlayerService pluginPlayerService = plugin.getPlayerService();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        pluginPlayerService.clearPlayer(player);
        player.getPersistentDataContainer().set(NamespacedKeys.CONNECTED.getNamespacedKey(), PersistentDataType.BOOLEAN, false);

        World world = createWorld(player);

        if (world == null) {
            player.kick(MiniMessage.miniMessage().deserialize("<red>Sorry, an error occurred while creating the connection world. Please contact an administrator."));
            plugin.getLogger().severe("An error occurred while creating the connection world for player " + player.getName());
            return;
        }

        player.teleport(world.getSpawnLocation());
        player.setFlying(true);
        PluginPlayer pluginPlayer = pluginPlayerService.loadFromUuid(player.getUniqueId());

        if (pluginPlayer == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(Config.MESSAGE_FIRST_JOIN_WELCOME.getString()));
            return;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize(Config.MESSAGE_JOIN_WELCOME.getString())); //TODO: rework this logic.
    }

    public static World createWorld(Player player) {
        WorldCreator wc = new WorldCreator("world_" + player.getUniqueId().toString());
        wc.generator(new VoidGenerator());
        wc.generateStructures(false);
        return wc.createWorld();
    }
}
