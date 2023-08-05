package salt.saltfun.LeavesDecay;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import salt.saltfun.Config;

public class LeavesDecayListener implements Listener {

    private LeavesDecay main;

    public LeavesDecayListener(LeavesDecay leavesDecay) {
        main = leavesDecay;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        var b = event.getBlock();
        if (Config.LeavesDecayListenBlockDestroy && main.isThatBlock(b) && !main.isScheduled(b)) {
            main.scheduleSurroundBlocks(b);
            main.startScheduler();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        var b = event.getBlock();
        if (Config.LeavesDecayListenDecay && !main.isScheduled(b)) {
            main.scheduleSurroundBlocks(b);
            main.startScheduler();

        }
    }
}
