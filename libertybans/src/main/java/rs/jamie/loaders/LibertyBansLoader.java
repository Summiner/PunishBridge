package rs.jamie.loaders;

import rs.jamie.modules.LibertyBansModule;
import rs.jamie.PunishDispatch;
import rs.jamie.modules.Module;

public class LibertyBansLoader implements Loader {


    @Override
    public boolean shouldLoad() {
        return isPluginLoaded("LibertyBans");
    }

    @Override
    public Module load(PunishDispatch punishDispatch) {
        return new LibertyBansModule(punishDispatch);
    }
}