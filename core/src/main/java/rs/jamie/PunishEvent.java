package rs.jamie;

public class PunishEvent {

    private final Punishment punishment;

    public PunishEvent(Punishment punishment) {
        this.punishment = punishment;
    }

    public Punishment getPunishment() {
        return this.punishment;
    }

}
