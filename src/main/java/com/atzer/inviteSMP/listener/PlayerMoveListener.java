package com.atzer.inviteSMP.listener;

import com.atzer.inviteSMP.InviteSMP;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

public final class PlayerMoveListener  implements Listener {

    private static final InviteSMP plugin = InviteSMP.getInstance();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "connected"), PersistentDataType.BOOLEAN, false)) {
            event.setCancelled(true);
        }
    }

}
