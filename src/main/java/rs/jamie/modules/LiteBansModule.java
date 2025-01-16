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

    public void register() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                PunishType punishType = PunishType.NULL;
                switch (entry.getType()) {
                    case "ban":
                        punishType = PunishType.BAN;
                        break;
                    case "mute":
                        punishType = PunishType.MUTE;
                        break;
                    case "warn":
                        punishType = PunishType.WARN;
                        break;
                    case "kick":
                        punishType = PunishType.KICK;
                        break;
                }
                Punishment punishment = new Punishment(false, punishType, UUID.fromString(entry.getUuid()), UUID.fromString(entry.getExecutorUUID()), entry.getReason(), entry.getServerOrigin());
                punishDispatch.dispatchEvent(new PunishEvent(punishment));
            }
            @Override
            public void entryRemoved(Entry entry) {
                PunishType punishType = PunishType.NULL;
                switch (entry.getType()) {
                    case "ban":
                        punishType = PunishType.BAN;
                        break;
                    case "mute":
                        punishType = PunishType.MUTE;
                        break;
                    case "warn":
                        punishType = PunishType.WARN;
                        break;
                }
                Punishment punishment = new Punishment(true, punishType, UUID.fromString(entry.getUuid()), UUID.fromString(entry.getRemovedByUUID()), entry.getReason(), entry.getServerOrigin());
                punishDispatch.dispatchEvent(new PunishEvent(punishment));
            }
        });

    }

}
