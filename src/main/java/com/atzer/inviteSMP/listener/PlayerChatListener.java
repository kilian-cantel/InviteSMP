package com.atzer.inviteSMP.listener;

import com.atzer.inviteSMP.InviteSMP;
import com.atzer.inviteSMP.NamespacedKeys;
import io.papermc.paper.event.player.AsyncChatEvent;
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

        }
    }

}
