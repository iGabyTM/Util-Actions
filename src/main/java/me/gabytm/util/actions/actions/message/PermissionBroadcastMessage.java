package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import me.gabytm.util.actions.utils.StringUtil;
import org.bukkit.entity.Player;

public class PermissionBroadcastMessage implements Action {
    @Override
    public String getID() {
        return "permissionbroadcast";
    }

    @Override
    public String getDescription() {
        return "Send a message to all online players if the have a said permission";
    }

    @Override
    public String getUsage() {
        return "[permissionbroadcast] permission.node Only players that have 'permission.node' can see this message";
    }

    @Override
    public void run(Player player, String data) {
        if (data.contains(" ")) {
            final String[] parts = data.split(" ", 2);
            StringUtil.broadcast(parts[1], parts[0]);
        }
    }
}
