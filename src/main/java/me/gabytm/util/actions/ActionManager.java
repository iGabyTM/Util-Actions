package me.gabytm.util.actions;

import me.gabytm.util.actions.actions.command.ConsoleCommand;
import me.gabytm.util.actions.actions.command.PlayerCommand;
import me.gabytm.util.actions.actions.message.BroadcastMessage;
import me.gabytm.util.actions.actions.message.ChatMessage;
import me.gabytm.util.actions.actions.message.PlayerChat;
import me.gabytm.util.actions.actions.misc.CloseInventory;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * TODO Add comments to each method / class
 */
public class ActionManager {
    private final Map<String, Action> actions = new HashMap<>();
    private final JavaPlugin plugin;

    public ActionManager(final JavaPlugin plugin) {
        this.plugin = plugin;

        Stream.of(
                new BroadcastMessage(),
                new ChatMessage(),
                new ConsoleCommand(),
                new CloseInventory(),
                new PlayerChat(),
                new PlayerCommand()
        ).forEach(this::register);
    }

    private String replacePlaceholders(final Player player, final String line) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return line;
        }

        return StringUtils.replace(line, "%player_name%", player.getName());
    }

    public void register(final Action action) {
        actions.put((action.getID() == null ? action.getClass().getSimpleName() : action.getID()).toUpperCase(), action);
        action.getAliases().forEach(alias -> actions.put(alias, action));
    }

    public void execute(final Player player, final String data) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String[] parts = ChatColor.translateAlternateColorCodes('&', data.trim()).split(" ", 2);

            if (parts.length == 1) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacePlaceholders(player, data));
                return;
            }

            final Action action = getAction(StringUtils.substringBetween(parts[0], "[", "]"));

            if (action == null) {
                return;
            }

            action.run(player, replacePlaceholders(player, parts[1]));
        });
    }

    public void execute(final Player player, final List<String> data) {
        data.forEach(d -> execute(player, d));
    }

    public void execute(final Player player, final String... data) {
        if (data == null) {
            return;
        }

        execute(player, Arrays.asList(data));
    }

    public Action getAction(final String id) {
        return actions.get(id.toUpperCase());
    }
}
