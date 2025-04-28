package rs.jamie.loaders;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import rs.jamie.PunishDispatch;
import rs.jamie.modules.Module;

public interface Loader {

    boolean shouldLoad();

    Module load(PunishDispatch punishDispatch);

    default boolean isPluginLoaded(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin!=null&&plugin.isEnabled();
    }

}
