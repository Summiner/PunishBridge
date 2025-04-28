package rs.jamie.modules;

import litebans.api.Database;
import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;

import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LiteBansModule implements Module {

    private final PunishDispatch punishDispatch;

    public LiteBansModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
    }

    @Override
    public void execute(Plugin plugin) {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                trigger(entry, false);
            }
            @Override
            public void entryRemoved(Entry entry) {
                trigger(entry, true);
            }
        });
    }

    private void trigger(Entry entry, boolean revoked) {
        PunishType punishType = switch (entry.getType()) {
            case "ban" -> PunishType.BAN;
            case "mute" -> PunishType.MUTE;
            case "warn" -> PunishType.WARN;
            case "kick" -> PunishType.KICK;
            default -> PunishType.NULL;
        };
        if(entry.getUuid()==null) return;
        UUID punished = UUID.fromString(entry.getUuid());
        UUID punisher;
        InetAddress address = null;
        try {
            address = InetAddress.getByName(entry.getIp());
        } catch (Exception e) {}
        if(Objects.equals(entry.getExecutorUUID(), "CONSOLE")) punisher = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");
        else punisher = entry.getExecutorUUID()!=null?UUID.fromString(entry.getExecutorUUID()):null;
        Punishment punishment = new Punishment(revoked, punishType, punished, address, punisher, entry.getReason(), entry.getDateEnd(), entry.getServerOrigin(), entry.isIpban());
        punishDispatch.dispatchEvent(new PunishEvent(punishment));
    }

    @Override
    public CompletableFuture<Boolean> isMuted(UUID uuid, @Nullable InetAddress address) {
        return CompletableFuture.supplyAsync(() -> Database.get().isPlayerMuted(uuid, address==null?null:address.toString()));
    }

    @Override
    public CompletableFuture<Boolean> isBanned(UUID uuid, @Nullable InetAddress address) {
        return CompletableFuture.supplyAsync(() -> Database.get().isPlayerBanned(uuid, address==null?null:address.toString()));
    }

}
