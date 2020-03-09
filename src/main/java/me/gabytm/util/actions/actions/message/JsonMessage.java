package me.gabytm.util.actions.actions.message;

import com.google.gson.JsonSyntaxException;
import me.gabytm.util.actions.Action;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JsonMessage implements Action {
    private final JavaPlugin plugin;

    public JsonMessage(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return "json";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return "[json]";
    }

    @Override
    public void run(Player player, String json) {
        try {
            BaseComponent[] components = ComponentSerializer.parse(json);
            player.spigot().sendMessage(components);
        } catch (JsonSyntaxException ignored) {
            plugin.getLogger().warning("[ActionUtil] Invalid JSON: '" + json + "'");
        }
    }
}
