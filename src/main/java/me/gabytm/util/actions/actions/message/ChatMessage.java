package me.gabytm.util.actions.actions.message;

import me.gabytm.util.actions.Action;
import org.bukkit.entity.Player;

public class ChatMessage implements Action {
    @Override
    public String getID() {
        return "message";
    }

    @Override
    public String getDescription() {
        return "Send player a message in chat";
    }

    @Override
    public String getUsage() {
        return "[message] Only the player can see this message";
    }

    @Override
    public void run(Player player, String data) {
        player.sendMessage(data);
    }
}
