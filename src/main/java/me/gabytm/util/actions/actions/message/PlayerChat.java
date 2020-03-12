package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerChat implements Action {
    private final Plugin plugin;

    public PlayerChat(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return "chat";
    }

    @Override
    public String getDescription() {
        return "Make the player to send a chat message";
    }

    @Override
    public String getUsage() {
        return "[chat] This message will be sent by the player";
    }

    @Override
    public void run(Player player, String data) {
        Bukkit.getScheduler().runTask(plugin, () -> player.chat(data));
    }
}
