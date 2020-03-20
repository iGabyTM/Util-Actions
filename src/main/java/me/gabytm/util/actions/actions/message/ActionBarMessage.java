package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import me.gabytm.util.actions.utils.ActionBarUtil;
import org.bukkit.entity.Player;

public class ActionBarMessage implements Action {
    @Override
    public String getID() {
        return "actionbar";
    }

    @Override
    public String getDescription() {
        return "Send an actionbar message to the player";
    }

    @Override
    public String getUsage() {
        return "[actionbar] This is a messae";
    }

    @Override
    public void run(Player player, String data) {
        ActionBarUtil.sendActionBar(player, data);
    }
}
