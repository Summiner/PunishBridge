package rs.jamie;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import rs.jamie.modules.AdvancedBanModule;
import rs.jamie.modules.LiteBansModule;

public class PunishBridge {

    private final PunishDispatch punishDispatch = new PunishDispatch();
    private final Plugin plugin;

    public PunishBridge(Plugin plugin) {
        this.plugin = plugin;
    }

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

        Plugin advancedban = Bukkit.getPluginManager().getPlugin("AdvancedBan");
        if(advancedban!=null&&advancedban.isEnabled()) {
            AdvancedBanModule advancedBanModule = new AdvancedBanModule(this.punishDispatch);
            Bukkit.getServer().getPluginManager().registerEvents(advancedBanModule, this.plugin);
        }


    }

    public PunishDispatch getDispatcher() {
        return this.punishDispatch;
    }
}