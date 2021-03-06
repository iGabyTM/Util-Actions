package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import me.gabytm.util.actions.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastMessage implements Action {
    @Override
    public String getID() {
        return "broadcast";
    }

    @Override
    public String getDescription() {
        return "Send a message to all online players";
    }

    @Override
    public String getUsage() {
        return "[broadcast] This is a broadcast";
    }

    @Override
    public void run(Player player, String message) {
        StringUtil.broadcast(message);
    }
}
