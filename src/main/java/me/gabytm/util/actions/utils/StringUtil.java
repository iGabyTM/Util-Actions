package me.gabytm.util.actions.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class StringUtil {
    public static String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void broadcast(final String message) {
        broadcast(message, null);
    }

    public static void broadcast(final String message, final String permission) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        final boolean checkPermission = permission != null && !permission.trim().isEmpty();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (checkPermission && !player.hasPermission(permission)) {
                continue;
            }

            player.sendMessage(message);
        }
    }
}
