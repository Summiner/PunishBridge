package rs.jamie.modules;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;

import java.util.UUID;

public class AdvancedBanModule implements Listener {

    private final PunishDispatch punishDispatch;

    public AdvancedBanModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
    }

    @EventHandler
    private void onBan(PunishmentEvent event) {
        PunishType punishType = PunishType.NULL;
        Boolean ip_ban = false;
        switch (event.getPunishment().getType()) {
            case BAN:
                punishType = PunishType.BAN;
                break;
            case TEMP_BAN:
                punishType = PunishType.BAN;
                break;
            case IP_BAN:
                punishType = PunishType.BAN;
                ip_ban = true;
                break;
            case TEMP_IP_BAN:
                punishType = PunishType.BAN;
                ip_ban = true;
                break;
            case MUTE:
                punishType = PunishType.MUTE;
            case TEMP_MUTE:
                punishType = PunishType.MUTE;
            case WARNING:
                punishType = PunishType.WARN;
            case TEMP_WARNING:
                punishType = PunishType.WARN;
            case KICK:
                punishType = PunishType.KICK;
        }
        System.out.println(event.getPunishment().getOperator());
        Punishment punishment = new Punishment(false, punishType, UUID.fromString(event.getPunishment().getUuid()), UUID.randomUUID(), event.getPunishment().getReason(), event.getPunishment().getEnd(), null, ip_ban);
        punishDispatch.dispatchEvent(new PunishEvent(punishment));
    }


}
