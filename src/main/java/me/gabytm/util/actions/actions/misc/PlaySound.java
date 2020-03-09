package me.gabytm.util.actions.actions.misc;

import com.google.common.primitives.Floats;
import me.gabytm.util.actions.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class PlaySound implements Action {
    final List<String> sounds = new LinkedList<>();

    public PlaySound() {
        for (Sound sound : Sound.values()) {
            sounds.add(sound.name());
        }
    }

    @Override
    public String getID() {
        return "sound";
    }

    @Override
    public String getDescription() {
        return "Play a sound at player's location, 'volume' and 'pitch' are optional";
    }

    @Override
    public String getUsage() {
        return "[sound] SOUND_NAME volume pitch";
    }

    @Override
    public void run(Player player, String data) {
        if (!data.contains(" ")) {
            return;
        }

        final String[] parts = data.toUpperCase().split(" ");

        if (!sounds.contains(parts[0])) {
            return;
        }

        final Sound sound = Sound.valueOf(parts[0]);
        final Float volume = parts.length > 1 ? Floats.tryParse(parts[1]) : null;
        final Float pitch = parts.length > 2 ? Floats.tryParse(parts[2]) : null;

        player.playSound(player.getLocation(), sound, volume == null ? 0f : volume, pitch == null ? 1.0f : pitch);
    }
}
