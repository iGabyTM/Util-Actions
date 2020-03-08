package me.gabytm.util.actions.actions.command;

import me.gabytm.util.actions.Action;
import org.bukkit.entity.Player;

public class PlayerCommand implements Action {

    @Override
    public String getID() {
        return "player";
    }

    @Override
    public void run(Player player, String data) {
        player.performCommand(data);
    }
}
