package rs.jamie.modules;

import litebans.api.Database;
import litebans.api.Entry;
import litebans.api.Events;
import me.leoko.advancedban.manager.PunishmentManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;

import java.net.InetSocketAddress;
import java.util.UUID;

public class LiteBansModule implements Module {

    private final PunishDispatch punishDispatch;

    public LiteBansModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
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
        UUID punisher = entry.getExecutorUUID()!=null?UUID.fromString(entry.getExecutorUUID()):null;
        Punishment punishment = new Punishment(revoked, punishType, punished, punisher, entry.getReason(), entry.getDateEnd(), entry.getServerOrigin(), entry.isIpban());
        punishDispatch.dispatchEvent(new PunishEvent(punishment));

    }

    public void register() {
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

    @Override
    public boolean isMuted(UUID uuid, @Nullable String ip) {
        //InetSocketAddress address = player.getAddress();
        //return Database.get().isPlayerMuted(player.getUniqueId(), address!=null?address.getAddress().toString():null);
        return Database.get().isPlayerMuted(uuid, ip);
    }

    @Override
    public boolean isBanned(UUID uuid, @Nullable String ip) {
        return Database.get().isPlayerBanned(uuid, ip);
    }

}
