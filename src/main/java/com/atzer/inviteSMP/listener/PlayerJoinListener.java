package com.atzer.inviteSMP.listener;

import com.atzer.inviteSMP.InviteSMP;
import com.atzer.inviteSMP.service.PluginPlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private static final InviteSMP plugin = InviteSMP.getInstance();
    private static final PluginPlayerService pluginPlayerService = plugin.getPlayerService();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {

    }

}
