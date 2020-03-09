package me.gabytm.util.actions.actions.misc;

import me.gabytm.util.actions.Action;
import org.bukkit.entity.Player;

public class CloseInventory implements Action {

    @Override
    public String getID() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Close player's inventory";
    }

    @Override
    public String getUsage() {
        return "[close]";
    }

    @Override
    public void run(Player player, String data) {
        player.closeInventory();
    }
}
