/*
 * @Author: Salt
 * @Description: 树叶快速凋亡功能
 */
package salt.saltfun.LeavesDecay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.scheduler.BukkitTask;

import salt.saltfun.Config;
import salt.saltfun.Saltfun;
import salt.saltfun.Utils;

/**
 * 主要逻辑
 * <p>
 * 有一个内部调度器，每隔一段时间执行一次主过程，使用startScheduler方法启动
 * <p>
 * 每次主过程中会检查记录的树叶方块，并判断是否需要凋亡
 * <p>
 * 每次主过程中会记录附近的树叶方块，在下一次执行时更新
 * <p>
 * 使用scheduleSurroundBlocks方法将某个方块附近一圈方块加入调度
 * <p>
 * 没有可更新方块时，调度器会自动停用
 */
public class LeavesDecay {
    private Saltfun instance;
    /** 记录一下内部调度器是否开启 */
    private boolean scheduled = false;
    /** 调度器任务 */
    private BukkitTask scheduledTask;
    /** 计划下一次更新的方块 */
    private final Set<Block> plannedBlocks = new HashSet<Block>();
    /** 用于仅播放一次声音的模式下，记录哪些位置播放过声音 */
    private final List<Location> soundPlayedLocations = new ArrayList<Location>();

    /**
     * 启用内部调度器
     * <p>
     * 会自动根据配置文件控制行为
     */
    public void startScheduler() {
        Utils.debug("[快速落叶] 尝试启动调度器");
        if (scheduled)
            return;
        scheduled = true;
        Utils.debug("[快速落叶] 调度器开始");
        // 等待8tick，让树叶的distance属性传播完成
        scheduledTask = Bukkit.getScheduler().runTaskLater(instance, this::schedulerTick, Config.LeavesDecayDecayDelay);
    }

    /**
     * 禁用内部调度器
     */
    public void stopScheduler() {
        scheduled = false;
        if (scheduledTask != null && !scheduledTask.isCancelled())
            scheduledTask.cancel();
    }

    /**
     * 内部调度器主要过程
     */
    private void schedulerTick() {
        // 配置禁用、插件禁用、没有可更新方块的情况下
        if (!Config.LeavesDecayEnable || instance == null || plannedBlocks.isEmpty()) {
            Utils.debug("[快速落叶] 调度器停止");
            stopScheduler();
            return;
        }
        long count = 0;
        var max = Config.LeavesDecayMaxBlocksOneTime;
        var successBlocks = new ArrayList<Block>();
        var successCount = 0;
        Utils.debug("[快速落叶] 调度器执行开始，计划的方块 " + plannedBlocks.size() + "，配置最多执行 " + max);
        // 对象锁
        // synchronized (plannedBlocks) {
        var checkedBlocks = new ArrayList<Block>();
        var it = plannedBlocks.iterator();
        while (it.hasNext()) {
            if (max > 0 && successCount >= max)
                break;
            // 获取已在计划中的方块
            var b = it.next();
            var success = leavesDecay(b);
            // 计数
            checkedBlocks.add(b);
            count++;
            // 如果凋亡成功，则将附近方块纳入计划
            if (success) {
                successCount++;
                successBlocks.add(b);
            }
        }
        plannedBlocks.removeAll(checkedBlocks);
        // }
        if (Config.LeavesDecayDecaySound == 1)
            soundPlayedLocations.clear();
        scheduleSurroundBlocks(successBlocks);
        Utils.debug("[快速落叶] 调度器执行完毕，检查的方块" + count + "，成功的方块" + successCount);
        scheduledTask = Bukkit.getScheduler().runTaskLater(instance, this::schedulerTick, Config.LeavesDecayDecayDelay);
    }

    /**
     * 判断方块是否已经加入了调度器
     * 
     * @param block 方块
     */
    public boolean isScheduled(Block block) {
        return plannedBlocks.contains(block);
    }

    /**
     * 这个方块是否为配置文件中的可凋零树叶
     * 
     * @param block 方块
     * @return
     */
    public boolean isThatLeaf(Block block) {
        if (Config.LeavesDecayLeaves.isEmpty())
            return true; // 置空表示没有限制
        return Config.LeavesDecayLeaves.contains(block.getType().name());
    }

    /**
     * 这个方块是否为配置文件中的需要监听的方块
     * 
     * @param block 方块
     * @return
     */
    public boolean isThatBlock(Block block) {
        if (Config.LeavesDecayBlocks.isEmpty()) {
            return Utils.isLeaf(block) || Utils.isLog(block); // 置空表示所有的木头和树叶
        }
        return Config.LeavesDecayBlocks.contains(block.getType().name());
    }

    /**
     * 将某个方块附近的树叶方块加入调度器
     * 
     * @param block 方块
     */
    public void scheduleSurroundBlocks(Block block) {
        if (block == null)
            return;
        var blocks = Utils.getSurroundBlocks(block).stream().filter(Utils::isLeaf)
                .filter(this::isThatLeaf).toList();
        plannedBlocks.addAll(blocks);
    }

    /**
     * 将某些方块附近的所有树叶方块加入调度器
     * 
     * @param blocks 方块列表
     */
    public void scheduleSurroundBlocks(List<Block> block) {
        if (block == null)
            return;
        var blocks = Utils.getSurroundBlocks(block).stream().filter(Utils::isLeaf)
                .filter(this::isThatLeaf).toList();
        plannedBlocks.addAll(blocks);
    }

    /**
     * 尝试执行树叶凋亡操作
     * 
     * @param block 要检查的方块
     * @return 是否成功执行树叶凋亡操作
     */
    public boolean leavesDecay(Block block) {
        // 检查方块
        if (!isThatLeaf(block))
            return false;
        if (!Utils.isDecayableLeaf(block))
            return false;
        // 尝试触发一个树叶凋亡事件
        LeavesDecayEvent event = new LeavesDecayEvent(block);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;
        // 树叶凋亡
        block.breakNaturally();
        // 声音与粒子
        playSound(block);
        displayPartials(block);
        // 成功执行树叶凋亡
        return true;
    }

    /**
     * 在方块位置播放破坏音效
     * <p>
     * 会自动查询配置文件确定音效控制
     * 
     * @param block 方块
     */
    public void playSound(Block block) {
        var type = Config.LeavesDecayDecaySound;
        // Long 不能用 Switch
        if (type == 0)
            return;
        var bl = block.getLocation();
        var bw = block.getWorld();
        if (type == 1) {
            // 只播放一次树叶凋零 但是可能存在多个人同时砍树
            // 因此解决方案是16格内只播放一次
            for (var l : soundPlayedLocations) {
                if (l.getWorld() == bw && bl.distance(l) < 16)
                    return;
            }
            soundPlayedLocations.add(bl);
        }
        bw.playSound(bl, Sound.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.05f, 1.2f);
    }

    /**
     * 在方块位置播放破坏的粒子效果
     * <p>
     * 会自动查询配置文件确定粒子数量
     * 
     * @param block 方块
     */
    public void displayPartials(Block block) {
        var type = Config.LeavesDecayDecayParticle;
        // Long 不能用 Switch
        if (type == 0)
            return;
        var bl = block.getLocation();
        var bw = block.getWorld();
        // 粒子数量
        int count = type == 1 ? 2 : 8;
        // if (type == 1) {
        // } else if (type == 2) {
        // }
        bw.spawnParticle(Particle.BLOCK_DUST, bl.add(0.5, 0.5, 0.5), count, 0.2, 0.2, 0.2, 0,
                block.getType().createBlockData());
    }

    public void onEnable(Saltfun plugin) {
        instance = plugin;
        var listener = new LeavesDecayListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void onDisable(Saltfun plugin) {
        plannedBlocks.clear();
        soundPlayedLocations.clear();
        stopScheduler();
    }

    public Saltfun getInstance() {
        return instance;
    }
}
