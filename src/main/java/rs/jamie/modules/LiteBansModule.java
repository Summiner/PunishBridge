package rs.jamie.modules;

import litebans.api.Entry;
import litebans.api.Events;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;

import java.util.UUID;

public class LiteBansModule {

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

}
