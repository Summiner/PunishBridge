package rs.jamie;

import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.UUID;

public record Punishment(Boolean revoked, PunishType punishType, @Nullable UUID punished, @Nullable InetAddress punished_address, @Nullable UUID punisher, String reason, @Nullable Long end, @Nullable String server, Boolean ipbased) {
}
