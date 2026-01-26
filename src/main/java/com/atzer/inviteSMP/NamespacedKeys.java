package com.atzer.inviteSMP;

import org.bukkit.NamespacedKey;

public enum NamespacedKeys {

    CONNECTED(new NamespacedKey(InviteSMP.getInstance(), "connected")),
    CONNECTING(new NamespacedKey(InviteSMP.getInstance(), "connecting")),
    AUTHENTICATING(new NamespacedKey(InviteSMP.getInstance(), "authenticating"));

    private final NamespacedKey namespacedKey;

    NamespacedKeys(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    public NamespacedKey getNamespacedKey() {
        return this.namespacedKey;
    }
}
