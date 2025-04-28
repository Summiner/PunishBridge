package rs.jamie.modules;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;
import rs.jamie.utils.FastUuidSansHyphens;

import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AdvancedBanModule implements Listener, Module {

    private final PunishDispatch punishDispatch;

    public AdvancedBanModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
    }

    @Override
    public void execute(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void trigger(me.leoko.advancedban.utils.Punishment punishment, boolean revoked) {
        PunishType punishType = PunishType.NULL;
        boolean ipBan = false;
        switch (punishment.getType()) {
            case BAN, TEMP_BAN:
                punishType = PunishType.BAN;
                break;
            case IP_BAN, TEMP_IP_BAN:
                punishType = PunishType.BAN;
                ipBan = true;
                break;
            case MUTE, TEMP_MUTE:
                punishType = PunishType.MUTE;
                break;
            case WARNING, TEMP_WARNING:
                punishType = PunishType.WARN;
                break;
            case KICK:
                punishType = PunishType.KICK;
                break;
        }
        UUID punished = FastUuidSansHyphens.parseUuid(punishment.getUuid());
        UUID punisher = Bukkit.getPlayerUniqueId(punishment.getOperator());
        Punishment p = new Punishment(revoked, punishType, punished, null, punisher, punishment.getReason(), punishment.getEnd(), null, ipBan);
        punishDispatch.dispatchEvent(new PunishEvent(p));

    }

    @EventHandler
    private void onUnban(RevokePunishmentEvent event) {
        trigger(event.getPunishment(), true);
    }

    @EventHandler
    private void onBan(PunishmentEvent event) {
        trigger(event.getPunishment(), false);
    }

    @Override
    public CompletableFuture<Boolean> isMuted(UUID uuid, @Nullable InetAddress address) {
        return CompletableFuture.supplyAsync(() -> PunishmentManager.get().isMuted(uuid.toString().replaceAll("-", "")));
    }

    @Override
    public CompletableFuture<Boolean> isBanned(UUID uuid, @Nullable InetAddress address) {
        return CompletableFuture.supplyAsync(() -> PunishmentManager.get().isBanned(uuid.toString().replaceAll("-", "")));
    }

}
