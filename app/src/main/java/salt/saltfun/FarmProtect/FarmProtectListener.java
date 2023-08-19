package salt.saltfun.FarmProtect;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import salt.saltfun.Config;
import salt.saltfun.Utils;

public class FarmProtectListener implements Listener {
    private FarmProtect main;

    public FarmProtectListener(FarmProtect farmProtect) {
        main = farmProtect;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent e) {
        if (!Config.FarmEnable || !Config.FarmPreventEntityDestroy)
            return;
        var block = e.getBlock();
        if (!Utils.isFarmland(block))
            return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!Config.FarmEnable || !Config.FarmPreventPlayerDestroy)
            return;
        if (e.getAction() != Action.PHYSICAL)
            return;
        var block = e.getClickedBlock();
        if (block == null || !Utils.isFarmland(block))
            return;
        e.setCancelled(true);
    }

}
