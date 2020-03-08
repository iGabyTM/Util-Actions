package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastMessage implements Action {
    @Override
    public String getID() {
        return "broadcast";
    }

    @Override
    public void run(Player player, String data) {
        Bukkit.broadcastMessage(data);
    }
}
