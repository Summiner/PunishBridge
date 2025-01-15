package rs.jamie;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import rs.jamie.modules.LiteBansModule;

public class PunishBridge {

    private final PunishDispatch punishDispatch = new PunishDispatch();

    public void start() {
//        punishDispatch.addListener(new PunishListener() {
//            @Override
//            public void onPunishment(PunishEvent event) {
//
//            }
//        });

        Plugin litebans = Bukkit.getPluginManager().getPlugin("LiteBans");
        if(litebans!=null&&litebans.isEnabled()) {
            LiteBansModule liteBansModule = new LiteBansModule(this.punishDispatch);
            liteBansModule.register();
        }


    }

    public PunishDispatch getDispatcher() {
        return this.punishDispatch;
    }
}