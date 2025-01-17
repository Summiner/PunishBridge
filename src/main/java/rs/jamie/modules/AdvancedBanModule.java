package rs.jamie.modules;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;
import rs.jamie.utils.FastUuidSansHyphens;

import java.util.UUID;

public class AdvancedBanModule implements Listener {

    private final PunishDispatch punishDispatch;

    public AdvancedBanModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
    }

    @EventHandler
    private void onBan(PunishmentEvent event) {
        PunishType punishType = PunishType.NULL;
        Boolean ipban = false;
        switch (event.getPunishment().getType()) {
            case BAN:
                punishType = PunishType.BAN;
                break;
            case TEMP_BAN:
                punishType = PunishType.BAN;
                break;
            case IP_BAN:
                punishType = PunishType.BAN;
                ipban = true;
                break;
            case TEMP_IP_BAN:
                punishType = PunishType.BAN;
                ipban = true;
                break;
            case MUTE, TEMP_MUTE:
                punishType = PunishType.MUTE;
                punishType = PunishType.MUTE;
                break;
            case WARNING, TEMP_WARNING:
                punishType = PunishType.WARN;
                break;
            case KICK:
                punishType = PunishType.KICK;
                break;
        }
        Punishment punishment = new Punishment(false, punishType, FastUuidSansHyphens.parseUuid(event.getPunishment().getUuid()), Bukkit.getPlayerUniqueId(event.getPunishment().getOperator()), event.getPunishment().getReason(), event.getPunishment().getEnd(), null, ipban);
        punishDispatch.dispatchEvent(new PunishEvent(punishment));
    }


}
