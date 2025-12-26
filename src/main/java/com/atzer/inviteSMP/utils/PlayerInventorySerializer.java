package com.atzer.inviteSMP.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public final class PlayerInventorySerializer {

    public static String serialize(ItemStack[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeInt(contents.length);

        for (ItemStack item : contents) {
            dataOutput.writeObject(item);
        }

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static ItemStack[] deserialize(String data) throws IOException, ClassNotFoundException {
        if (data == null || data.isEmpty()) {
            return new ItemStack[0];
        }

        byte[] bytes = Base64.getDecoder().decode(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        int size = dataInput.readInt();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            items[i] = (ItemStack) dataInput.readObject();
        }

        return items;
    }
}
