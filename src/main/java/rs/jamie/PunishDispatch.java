package rs.jamie;

import java.util.ArrayList;
import java.util.List;

public class PunishDispatch {

    private List<PunishListener> listeners = new ArrayList<>();

    public void addListener(PunishListener listener) {
        listeners.add(listener);
    }

    public void dispatchEvent(PunishEvent event) {
        for (PunishListener listener : listeners) {
            listener.onPunishment(event);
        }
    }

}
