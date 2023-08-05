package salt.saltfun;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    /** 树叶凋亡模块是否开启 */
    public static boolean LeavesDecayEnable = true;
    /** 树叶凋亡延迟 */
    public static long LeavesDecayDecayDelay = 2;
    /** 每次树叶凋亡最多破坏多少方块 小于0禁用这个限制 */
    public static long LeavesDecayMaxBlocksOneTime = 2;
    /** 树叶凋亡音效，0 没有音效，1 树叶同时凋亡只触发一次音效，2 每个树叶凋亡时都触发音效 */
    public static long LeavesDecayDecaySound = 1;
    /** 树叶凋亡粒子效果，0 没有粒子，1 较少粒子，2 完整粒子效果（可能会卡顿） */
    public static long LeavesDecayDecayParticle = 1;
    /** 树叶凋亡 监听树叶自然凋亡事件 */
    public static boolean LeavesDecayListenDecay = true;
    /** 树叶凋亡 监听方块破坏事件 */
    public static boolean LeavesDecayListenBlockDestroy = true;
    /** 树叶凋亡 监听哪些方块 */
    public static final Set<String> LeavesDecayBlocks = new HashSet<String>();
    /** 树叶凋亡 哪些树叶可以凋亡 */
    public static final Set<String> LeavesDecayLeaves = new HashSet<String>();

    /**
     * 重新加载树叶凋亡相关配置
     * 
     * @param config 配置文件，可以通过Config.getConfig()获取
     */
    public static void loadLeavesDecayConfig(FileConfiguration config) {
        var prefix = "leaves-decay.";
        LeavesDecayEnable = config.getBoolean(prefix + "enable", true);
        LeavesDecayDecayDelay = Utils.clamp(config.getLong(prefix + "decay-delay", 2), 1, 9000000000000l);
        LeavesDecayMaxBlocksOneTime = Utils.clamp(config.getLong(prefix + "max-blocks-one-time", 2), 1,
                9000000000000l);
        LeavesDecayDecaySound = Utils.clamp(config.getLong(prefix + "decay-sound", 1), 0, 2);
        LeavesDecayDecayParticle = Utils.clamp(config.getLong(prefix + "decay-particle", 1), 0, 2);
        LeavesDecayListenDecay = config.getBoolean(prefix + "listen-decay", true);
        LeavesDecayListenBlockDestroy = config.getBoolean(prefix + "block-destroy", true);
        LeavesDecayBlocks.clear();
        LeavesDecayBlocks
                .addAll(config.getStringList(prefix + "blocks").stream().map((var s) -> s.toUpperCase()).toList());
        LeavesDecayLeaves.clear();
        LeavesDecayLeaves
                .addAll(config.getStringList(prefix + "leaves").stream().map((var s) -> s.toUpperCase()).toList());
        Utils.debug(
                "FarmEnable: " + FarmEnable,
                "LeavesDecayEnable: " + LeavesDecayEnable,
                "LeavesDecayDecayDelay: " + LeavesDecayDecayDelay,
                "LeavesDecayMaxBlocksOneTime: " + LeavesDecayMaxBlocksOneTime,
                "LeavesDecayDecaySound: " + LeavesDecayDecaySound,
                "LeavesDecayDecayParticle: " + LeavesDecayDecayParticle,
                "LeavesDecayListenDecay: " + LeavesDecayListenDecay,
                "LeavesDecayListenBlockDestroy: " + LeavesDecayListenBlockDestroy,
                "LeavesDecayBlocks: " + LeavesDecayBlocks.size() + "个",
                "LeavesDecayLeaves: " + LeavesDecayLeaves.size() + "个");
    }

    /** 耕地保护模块是否开启 */
    public static boolean FarmEnable = true;
    /** 耕地保护 是否允许玩家踩坏耕地 */
    public static boolean FarmPreventPlayerDestroy = true;
    /** 耕地保护 是否允许其他实体踩坏耕地 */
    public static boolean FarmPreventEntityDestroy = true;

    /**
     * 重新加载耕地保护相关配置
     * 
     * @param config 配置文件，可以通过Config.getConfig()获取
     */
    public static void loadFarmConfig(FileConfiguration config) {
        var prefix = "farm.";
        FarmEnable = config.getBoolean(prefix + "enable", true);
        FarmPreventPlayerDestroy = config.getBoolean(prefix + "prevent-player-destroy", true);
        FarmPreventEntityDestroy = config.getBoolean(prefix + "prevent-entity-destroy", true);
        Utils.debug(
                "FarmEnable: " + FarmEnable,
                "FarmPreventPlayerDestroy: " + FarmPreventPlayerDestroy,
                "FarmPreventEntityDestroy: " + FarmPreventEntityDestroy);
    }

    public static boolean debug = false;

    /**
     * 重新加载基本配置
     * 
     * @param config 配置文件，可以通过Config.getConfig()获取
     */
    public static void loadGeneralConfig(FileConfiguration config) {
        debug = config.getBoolean("debug", false);
        Utils.debug("debug: " + debug);
    }

    /**
     * 获取基本配置文件
     * 
     * @param _plugin Saltfun插件实例，如果传入null则会自行获取
     * @return 配置文件
     */
    public static void readConfig(Saltfun _plugin) {
        var plugin = _plugin == null ? Saltfun.getInstance() : _plugin;
        var config = getConfig(plugin);
        plugin.saveDefaultConfig();
        // 基本配置
        loadGeneralConfig(config);
        // 树叶凋亡模块
        loadLeavesDecayConfig(config);
        // 耕地保护模块
        loadFarmConfig(config);
    }

    /**
     * 获取基本配置文件
     * 
     * @param _plugin Saltfun插件实例，如果传入null则会自行获取
     * @return 配置文件
     */
    public static FileConfiguration getConfig(Saltfun _plugin) {
        var plugin = _plugin == null ? Saltfun.getInstance() : _plugin;
        plugin.reloadConfig();
        return plugin.getConfig();
    }

}
