package me.gabytm.util.actions.actions.command;

import me.gabytm.util.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleCommand implements Action {
    private final JavaPlugin plugin;

    public ConsoleCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return "console";
    }

    @Override
    public String getDescription() {
        return "Run a command from console";
    }

    @Override
    public String getUsage() {
        return "[console] say This command is run by the console";
    }

    @Override
    public void run(Player player, String data) {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data));
    }
}
