package me.gabytm.util.actions.actions.command;

import me.gabytm.util.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionCommand implements Action {
    private final JavaPlugin plugin;

    public PermissionCommand(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return "permission";
    }

    @Override
    public String getDescription() {
        return "Bypass player's permission by adding a temporally permission";
    }

    @Override
    public String getUsage() {
        return "[permission] essentials.fly fly";
    }

    @Override
    public void run(Player player, String data) {
        if (!data.contains(" ")) {
            return;
        }

        final String[] parts = data.split(" ", 2);

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.hasPermission(parts[0])) {
                player.chat("/" + parts[1]);
                return;
            }

            final PermissionAttachment attachment = player.addAttachment(plugin, parts[0], true);
            player.chat("/" + parts[1]);
            player.removeAttachment(attachment);
        });
    }
}
