package rs.jamie;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import rs.jamie.loaders.Loader;
import rs.jamie.modules.BackupModule;
import rs.jamie.modules.Module;

import java.net.InetAddress;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class PunishBridge {

    private final PunishDispatch punishDispatch = new PunishDispatch();
    private final Plugin plugin;
    private Module module;

    public PunishBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        ServiceLoader<Loader> loader = ServiceLoader.load(Loader.class, Loader.class.getClassLoader());
        Logger logger = plugin.getLogger();
        logger.info("Found "+loader.stream().toList().size()+" service providers");
        module = new BackupModule();
        for (Loader service : loader) {
            logger.info("Discovered provider: "+service.getClass().getName());
            if(service.shouldLoad()) {
                module = service.load(punishDispatch);
            }
        }
        module.execute(plugin);
        logger.info("Using provider: "+module.getClass().getName());
    }

    public PunishDispatch getDispatcher() {
        return this.punishDispatch;
    }

    public CompletableFuture<Boolean> isMuted(UUID uuid, @Nullable InetAddress address) {
        return module.isMuted(uuid, address);
    }

    public CompletableFuture<Boolean> isBanned(UUID uuid, @Nullable InetAddress address) {
        return module.isBanned(uuid, address);
    }
}