package me.gabytm.util.actions;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gabytm.util.actions.actions.command.ConsoleCommand;
import me.gabytm.util.actions.actions.command.PermissionCommand;
import me.gabytm.util.actions.actions.command.PlayerCommand;
import me.gabytm.util.actions.actions.message.BroadcastMessage;
import me.gabytm.util.actions.actions.message.ChatMessage;
import me.gabytm.util.actions.actions.message.JsonMessage;
import me.gabytm.util.actions.actions.message.PlayerChat;
import me.gabytm.util.actions.actions.misc.CloseInventory;
import me.gabytm.util.actions.actions.misc.PlaySound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ActionManager {
    private final Map<String, Action> actions = new HashMap<>();
    private final Plugin plugin;

    public ActionManager(final Plugin plugin) {
        this.plugin = plugin;

        Stream.of(
                new BroadcastMessage(),
                new ChatMessage(),
                new ConsoleCommand(plugin),
                new CloseInventory(),
                new JsonMessage(plugin),
                new PermissionCommand(plugin),
                new PlayerChat(plugin),
                new PlayerCommand(plugin),
                new PlaySound()
        ).forEach(action -> register(action, true));
    }

    /**
     * Method used to replace the placeholders from the given string
     * If {@link PlaceholderAPI} isn't installed only %player_name%
     * will be replaced by player's name
     *
     * @param player the player
     * @param line   the string that will be formatted
     * @return formatted string with placeholders replaced by values
     */
    private String replacePlaceholders(final Player player, final String line) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', line.trim()));
        }

        return StringUtils.replace(ChatColor.translateAlternateColorCodes('&', line.trim()), "%player_name%", player.getName());
    }

    /**
     * Attempt to execute the given action
     *
     * @param player player
     * @param data   action
     */
    private void executeAction(final Player player, final String data) {
        final String line = replacePlaceholders(player, data);

        if (!line.contains(" ")) {
            final Action action = getAction(StringUtils.substringBetween(line, "[", "]"));

            if (action == null) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line);
                return;
            }

            action.run(player, line);
            return;
        }

        final String[] parts = line.split(" ", 2);
        final Action action = getAction(StringUtils.substringBetween(parts[0], "[", "]"));

        if (action == null) {
            return;
        }

        action.run(player, parts[1]);
    }

    /**
     * Register a new action
     *
     * @param action   the action that will be registered
     * @param override if true new actions will take priority if another
     *                 action is registered with the same id or alias
     */
    public void register(final Action action, final boolean override) {
        final String id = (action.getID() == null ? action.getClass().getSimpleName() : action.getID()).toUpperCase();

        if (override) {
            if (actions.get(id) != null) {
                plugin.getLogger().warning("[ActionUtil] Overriding the action with ID '" + id + "'");
            }

            actions.put(id, action);
            action.getAliases().forEach(alias -> actions.put(alias.toUpperCase(), action));
        } else {
            actions.putIfAbsent(id, action);
            action.getAliases().forEach(alias -> actions.putIfAbsent(alias.toUpperCase(), action));
        }
    }

    /**
     * Execute the given action
     *
     * @param player player
     * @param action action
     */
    public void execute(final Player player, final String action) {
        Bukkit.getScheduler().runTask(plugin, () -> executeAction(player, action));
    }

    /**
     * Execute the given action asynchronously
     *
     * @param player player
     * @param action action
     */
    public void executeAsync(final Player player, final String action) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> executeAction(player, action));
    }

    /**
     * Execute the given actions
     *
     * @param player  player
     * @param actions actions list
     */
    public void execute(final Player player, final List<String> actions) {
        actions.forEach(action -> execute(player, action));
    }

    /**
     * Execute the given actions asynchronously
     *
     * @param player  player
     * @param actions actions list
     */
    public void executeAsync(final Player player, final List<String> actions) {
        actions.forEach(action -> executeAsync(player, action));
    }

    /**
     * Execute the given actions
     *
     * @param player  player
     * @param actions actions array
     */
    public void execute(final Player player, final String... actions) {
        if (actions == null) {
            return;
        }

        execute(player, Arrays.asList(actions));
    }

    /**
     * Execute the given actions asynchronously
     *
     * @param player  player
     * @param actions actions array
     */
    public void executeAsync(final Player player, final String... actions) {
        if (actions == null) {
            return;
        }

        executeAsync(player, Arrays.asList(actions));
    }

    /**
     * Get an action by id / alias
     *
     * @param id action id / alias
     * @return the action with the corresponding id / alias
     * or null if no action was found
     */
    public Action getAction(final String id) {
        return actions.get(id.toUpperCase());
    }

    /**
     * Get all loaded actions
     *
     * @return actions map
     */
    public Map<String, Action> getActions() {
        return actions;
    }
}
