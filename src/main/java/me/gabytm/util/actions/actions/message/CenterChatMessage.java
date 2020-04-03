package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import me.gabytm.util.actions.utils.DefaultFontInfo;
import org.bukkit.entity.Player;

public class CenterChatMessage implements Action {
    @Override
    public String getID() {
        return "centermessage";
    }

    @Override
    public void run(Player player, String message) {
        player.sendMessage(DefaultFontInfo.centerMessage(message));
    }
}
