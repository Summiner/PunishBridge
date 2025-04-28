package rs.jamie.loaders;

import rs.jamie.PunishDispatch;
import rs.jamie.modules.LiteBansModule;
import rs.jamie.modules.Module;


public class LiteBansLoader implements Loader {


    @Override
    public boolean shouldLoad() {
        return isPluginLoaded("LiteBans");
    }

    @Override
    public Module load(PunishDispatch punishDispatch) {
        return new LiteBansModule(punishDispatch);
    }
}