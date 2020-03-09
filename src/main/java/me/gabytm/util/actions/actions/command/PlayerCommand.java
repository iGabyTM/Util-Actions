package me.gabytm.util.actions.actions.command;

import me.gabytm.util.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerCommand implements Action {
    private final JavaPlugin plugin;

    public PlayerCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "Make the player run a command if they have the required permission";
    }

    @Override
    public String getUsage() {
        return "[player] spawn";
    }

    @Override
    public void run(Player player, String data) {
        Bukkit.getScheduler().runTask(plugin, () -> player.chat("/" + data));
    }
}
