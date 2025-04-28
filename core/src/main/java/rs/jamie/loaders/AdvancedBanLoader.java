package rs.jamie.loaders;

import rs.jamie.PunishDispatch;
import rs.jamie.modules.AdvancedBanModule;
import rs.jamie.modules.Module;


public class AdvancedBanLoader implements Loader {


    @Override
    public boolean shouldLoad() {
        return isPluginLoaded("AdvancedBan");
    }

    @Override
    public Module load(PunishDispatch punishDispatch) {
        return new AdvancedBanModule(punishDispatch);
    }
}
