package com.atzer.inviteSMP.listener;

import com.atzer.inviteSMP.InviteSMP;
import com.atzer.inviteSMP.NamespacedKeys;
import com.atzer.inviteSMP.database.model.PluginPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class PlayerChatListener implements Listener {

    private static final InviteSMP plugin = InviteSMP.getInstance();

    @EventHandler
    public void onPlayerChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (Boolean.TRUE.equals(player.getPersistentDataContainer().get(NamespacedKeys.CONNECTING.getNamespacedKey(), PersistentDataType.BOOLEAN))) {
            String message = PlainTextComponentSerializer.plainText().serialize(event.message());
            String[] split = message.split(" ");

            if (split.length != 2) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>The format is invalid ! You must use: <identifier> <password>"));
                event.setCancelled(true);
                return;
            }

            String identifier = split[0];
            String password = split[1];

            PluginPlayer pluginPlayer = plugin.getPlayerService().login(identifier, password);

            if (pluginPlayer == null) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>The account could not be found with these credentials !"));
                event.setCancelled(true);
                return;
            }

            player.getPersistentDataContainer().set(NamespacedKeys.CONNECTING.getNamespacedKey(), PersistentDataType.BOOLEAN, false);
            player.getPersistentDataContainer().set(NamespacedKeys.CONNECTED.getNamespacedKey(), PersistentDataType.BOOLEAN, true);
        }
    }

}
