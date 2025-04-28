package rs.jamie.modules;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BackupModule implements Module {

    @Override
    public void execute(Plugin plugin) {

    }

    @Override
    public CompletableFuture<Boolean> isBanned(UUID uuid, @Nullable InetAddress address) {
        boolean banned = Bukkit.getOfflinePlayer(uuid).isBanned();
        return CompletableFuture.completedFuture(banned);
    }

}
