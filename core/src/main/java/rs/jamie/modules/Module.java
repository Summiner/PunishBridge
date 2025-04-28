package rs.jamie.modules;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Module {

    void execute(Plugin plugin);

    default CompletableFuture<Boolean> isMuted(UUID uuid, @Nullable InetAddress address) {
        return CompletableFuture.completedFuture(false);
    }

    default CompletableFuture<Boolean> isBanned(UUID uuid, @Nullable InetAddress address) {
        return CompletableFuture.completedFuture(false);
    }

}
