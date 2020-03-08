package me.gabytm.util.actions;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public interface Action {
    String getID();

    default List<String> getAliases() {
        return Collections.emptyList();
    }

    void run(final Player player, final String data);
}
