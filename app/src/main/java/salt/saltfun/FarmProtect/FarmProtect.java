package salt.saltfun.FarmProtect;

import org.bukkit.Bukkit;

import salt.saltfun.Saltfun;

public class FarmProtect {
    private Saltfun instance;

    public void onEnable(Saltfun plugin) {
        instance = plugin;
        var listener = new FarmProtectListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void onDisable(Saltfun plugin) {
    }
}
