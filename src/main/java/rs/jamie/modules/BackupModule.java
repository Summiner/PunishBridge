package rs.jamie.modules;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BackupModule implements Module {

    @Override
    public boolean isBanned(UUID uuid, @Nullable String ip) {
        return Bukkit.getOfflinePlayer(uuid).isBanned();
    }

}
