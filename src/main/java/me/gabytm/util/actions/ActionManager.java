package me.gabytm.util.actions;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gabytm.util.actions.actions.command.ConsoleCommand;
import me.gabytm.util.actions.actions.command.PermissionCommand;
import me.gabytm.util.actions.actions.command.PlayerCommand;
import me.gabytm.util.actions.actions.message.*;
import me.gabytm.util.actions.actions.misc.CloseInventory;
import me.gabytm.util.actions.actions.misc.PlaySound;
import me.gabytm.util.actions.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionManager {
    private final Map<String, Action> actions = new HashMap<>();
    private final Pattern RANDOM_PATTERN = Pattern.compile("\\{random:(?<values>((\\d+),?)+)}");
    private final Plugin plugin;

    /**
     * @param plugin   the plugin used to run tasks async
     * @param defaults load the default actions or not
     */
    public ActionManager(final Plugin plugin, final boolean defaults) {
        this.plugin = plugin;

        if (defaults) {
            loadDefaults();
        }
    }

    public ActionManager(final Plugin plugin) {
        this(plugin, true);
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
            return PlaceholderAPI.setPlaceholders(player, StringUtil.color(line.trim()));
        }

        return StringUtils.replace(StringUtil.color(line.trim()), "%player_name%", player.getName());
    }

    /**
     * Replace the custom tags with values
     *
     * Available tags:
     *   * {random:NUMBER,NUMBER} {@link #RANDOM_PATTERN}
     *       - if two values are passed a random number between
     *         first and second number will be generated
     *       - if more than two values are passed, one will be picked
     *
     * @param line the line where the tags will be replaced
     * @return formatted {@param line}
     */
    private String replaceTags(String line) {
        final Matcher randomMatcher = RANDOM_PATTERN.matcher(line);

        while (randomMatcher.find()) {
            final List<Integer> numbers = Stream.of(randomMatcher.group("values").split(","))
                    .map(Ints::tryParse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            int number = 0;

            if (numbers.size() == 1) {
                number = numbers.get(0);
            } else if (numbers.size() == 2) {
                number = new SplittableRandom().nextInt(numbers.get(0), numbers.get(1) + 1);
            } else if (numbers.size() > 2) {
                number = numbers.get(new SplittableRandom().nextInt(numbers.size()));
            }

            line = StringUtils.replace(line, randomMatcher.group(0), Integer.toString(number));
        }

        return line;
    }

    /**
     * Attempt to execute the given action
     *
     * @param player player
     * @param data   action
     */
    private void executeAction(final Player player, final String data) {
        final String line = replaceTags(replacePlaceholders(player, data));

        if (!line.contains(" ")) {
            final Action Action = getAction(StringUtils.substringBetween(line, "[", "]"));

            if (Action == null) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line);
                return;
            }

            Action.run(player, line);
            return;
        }

        final String[] parts = line.split(" ", 2);
        final Action Action = getAction(StringUtils.substringBetween(parts[0], "[", "]"));

        if (Action == null) {
            return;
        }

        Action.run(player, parts[1]);
    }

    /**
     * Load the default actions
     */
    public void loadDefaults() {
        Stream.of(
                new ActionBarMessage(),
                new BroadcastMessage(),
                new CenterBroadcastMessage(),
                new CenterChatMessage(),
                new ChatMessage(),
                new ConsoleCommand(plugin),
                new CloseInventory(),
                new JsonMessage(plugin),
                new PermissionBroadcastMessage(),
                new PermissionCommand(plugin),
                new PlayerChat(plugin),
                new PlayerCommand(plugin),
                new PlaySound()
        ).forEach(action -> register(action, true));
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

    public void unregister(final String id, final boolean aliases) {
        final Action Action = getAction(id);

        if (Action == null) {
            return;
        }

        actions.remove(id);

        if (!aliases || Action.getAliases().size() == 0) {
            return;
        }

        for (String alias : Action.getAliases()) {
            final Action act = actions.get(alias);

            if (act == null) {
                continue;
            }

            if (act.getID().equals(Action.getID()) && act.getAliases().equals(Action.getAliases()) && act.getDescription().equals(Action.getDescription())) {
                actions.remove(alias);
            }
        }
    }

    public void unregister(final String action) {
        unregister(action, true);
    }

    public void unregister(final List<String> actions, final boolean aliases) {
        for (String action : actions) {
            if (action == null) {
                continue;
            }

            unregister(action, aliases);
        }
    }

    public void unregister(final List<String> actions) {
        unregister(actions, true);
    }

    public void unregister(final boolean aliases, final String... actions) {
        unregister(Arrays.asList(actions), aliases);
    }

    public void unregister(final String... actions) {
        unregister(Arrays.asList(actions), true);
    }

    /**
     * Unregister all actions
     */
    public void unregisterAll() {
        actions.clear();
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
        execute(player, Arrays.asList(actions));
    }

    /**
     * Execute the given actions asynchronously
     *
     * @param player  player
     * @param actions actions array
     */
    public void executeAsync(final Player player, final String... actions) {
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
     * @return immutable copy of the actions map
     */
    public Map<String, Action> getActions() {
        return ImmutableMap.copyOf(actions);
    }
}
