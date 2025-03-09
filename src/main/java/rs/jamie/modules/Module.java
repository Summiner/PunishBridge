package rs.jamie.modules;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Module {

     default boolean isMuted(UUID uuid, @Nullable String ip) {
        return false;
    }

    default boolean isBanned(UUID uuid, @Nullable String ip) {
        return false;
    }

}
