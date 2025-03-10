package rs.jamie;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import rs.jamie.modules.AdvancedBanModule;
import rs.jamie.modules.BackupModule;
import rs.jamie.modules.LiteBansModule;
import rs.jamie.modules.Module;

import java.util.UUID;

public class PunishBridge {

    private final PunishDispatch punishDispatch = new PunishDispatch();
    private final Plugin plugin;
    private Module module;

    public PunishBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        module = new BackupModule(); // Backup module incase no punishment system is loaded

        Plugin litebans = Bukkit.getPluginManager().getPlugin("LiteBans");
        if(litebans!=null&&litebans.isEnabled()) {
            LiteBansModule liteBansModule = new LiteBansModule(this.punishDispatch);
            module = liteBansModule;
            liteBansModule.register();
        }

        Plugin advancedban = Bukkit.getPluginManager().getPlugin("AdvancedBan");
        if(advancedban!=null&&advancedban.isEnabled()) {
            AdvancedBanModule advancedBanModule = new AdvancedBanModule(this.punishDispatch);
            module = advancedBanModule;
            Bukkit.getServer().getPluginManager().registerEvents(advancedBanModule, this.plugin);
        }
    }

    public PunishDispatch getDispatcher() {
        return this.punishDispatch;
    }

    public boolean isMuted(UUID uuid, @Nullable String ip) {
        return module.isMuted(uuid, ip);
    }

    public boolean isBanned(UUID uuid, @Nullable String ip) {
        return module.isBanned(uuid, ip);
    }
}