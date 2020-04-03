package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import me.gabytm.util.actions.utils.DefaultFontInfo;
import me.gabytm.util.actions.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CenterBroadcastMessage implements Action {
    @Override
    public String getID() {
        return "centerbroadcast";
    }

    @Override
    public void run(Player player, String message) {
        StringUtil.broadcast(DefaultFontInfo.centerMessage(message));
    }
}
