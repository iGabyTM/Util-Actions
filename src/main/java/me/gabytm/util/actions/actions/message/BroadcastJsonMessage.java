import me.gabytm.util.actions.Action;
import me.gabytm.util.actions.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastJsonMessage implements Action {
    @Override
    public String getID() {
        return "jsonbroadcast";
    }

    @Override
    public String getDescription() {
        return "Send a json message to all online players";
    }

    @Override
    public String getUsage() {
        // Add a json broadcast usage
        return "[jsonbroadcast] This is a json broadcast";
    }

    @Override
    public void run(Player player, String message) {
        for (Player plr : Bukkit.getServer().getOnlinePlayers()) {
            plr.spigot().sendMessage(ComponentSerializer.parse(message));
        }
    }
}
