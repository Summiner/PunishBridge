package rs.jamie;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record Punishment(Boolean revoked, PunishType punishType, UUID punished, UUID punisher, String reason, @Nullable Long end, @Nullable String server, Boolean ipban) {
}
