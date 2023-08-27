package salt.saltfun.LeavesDecay;

import org.bukkit.Bukkit;
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
            Bukkit.getScheduler().runTaskLater(main.getInstance(), () -> {
                main.scheduleSurroundBlocks(b);
                main.startScheduler();
            }, 8l); // 树叶是否可腐烂（distance）的数值传播需要时间
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
