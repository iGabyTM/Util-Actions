package me.gabytm.util.actions.actions.command;

import me.gabytm.util.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommand implements Action {

    @Override
    public String getID() {
        return "console";
    }

    @Override
    public void run(Player player, String data) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
    }
}
