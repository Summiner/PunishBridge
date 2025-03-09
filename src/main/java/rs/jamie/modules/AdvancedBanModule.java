package rs.jamie.modules;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishEvent;
import rs.jamie.PunishType;
import rs.jamie.Punishment;
import rs.jamie.utils.FastUuidSansHyphens;

import java.util.UUID;

public class AdvancedBanModule implements Listener, Module {

    private final PunishDispatch punishDispatch;

    public AdvancedBanModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
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
        Punishment p = new Punishment(revoked, punishType, punished, punisher, punishment.getReason(), punishment.getEnd(), null, ipBan);
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
    public boolean isMuted(UUID uuid, @Nullable String ip) {
        return PunishmentManager.get().isMuted(uuid.toString());
    }

    @Override
    public boolean isBanned(UUID uuid, @Nullable String ip) {
        return PunishmentManager.get().isBanned(uuid.toString());
    }

}
