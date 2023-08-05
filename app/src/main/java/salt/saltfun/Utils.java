package salt.saltfun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.jetbrains.annotations.NotNull;

public class Utils {

    /** @deprecated 检查一个树叶方块是否可以掉落 */
    public static boolean isDecayable(@NotNull Leaves l) {
        return !l.isPersistent() && l.getDistance() > 6;
    }

    /**
     * 检查一个方块是否为树叶方块
     * 
     * @param block 方块
     * @return 如果为`true`则为树叶方块
     */
    public static boolean isLeaf(@NotNull Block b) {
        return Tag.LEAVES.isTagged(b.getType());
    }

    /**
     * 检查一个方块是否为原木/木头方块
     * 
     * @param block 方块
     * @return 如果为`true`则为原木/木头方块
     */
    public static boolean isLog(@NotNull Block b) {
        return Tag.LOGS.isTagged(b.getType());
    }

    /**
     * 检查一个方块是否为可以凋亡的树叶方块
     * 
     * @param block 方块
     * @return 如果为`true`则为可以凋亡
     */
    public static boolean isDecayableLeaf(@NotNull Block block) {
        if (!Tag.LEAVES.isTagged(block.getType()))
            return false;
        var l = (Leaves) block.getBlockData();
        return !l.isPersistent() && l.getDistance() > 6;
    }

    private static final List<BlockFace> surroundFace = Arrays
            .asList(BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH);

    /**
     * 获取一个方块上下东南西北6个方块
     * 
     * @param block 方块
     * @return 此方块上下东南西北6个位置的方块合集
     */
    public static List<Block> getSurroundBlocks(@NotNull Block block) {
        var blocks = new ArrayList<Block>();
        for (var face : surroundFace) {
            blocks.add(block.getRelative(face));
        }
        return blocks;
    }

    /**
     * 获取大量方块的上下东南西北6个方向的所有方块合集
     * 
     * @param block 方块
     * @return 这些方块上下东南西北6个位置的方块合集，不会重复，但是顺序可能不对
     */
    public static List<Block> getSurroundBlocks(@NotNull List<Block> blocks) {
        var blockSet = new HashSet<Block>();
        for (var block : blocks) {
            for (var face : surroundFace) {
                blockSet.add(block.getRelative(face));
            }
        }
        return new ArrayList<Block>(blockSet);
    }

    /**
     * 比对方块内部名称是否就是这个字符串
     * 
     * @param block 方块
     * @param str   方块名，不区分大小写
     */
    public static boolean isSameBlock(@NotNull Block block, String str) {
        var m = block.getType().name();
        return m.equalsIgnoreCase(str);
    }
    // /**
    // * 限制数字在一个固定范围里
    // *
    // * @param raw 原始值
    // * @param min 最小值
    // * @param max 最大值
    // * @return 修正后的数值
    // */
    // public static <T extends Number> T clamp(T raw, T min, T max) {
    // var res = raw;
    // if (res < min)
    // res = min;
    // else if (res > max)
    // res = max;
    // return res;
    // }

    /**
     * 限制数字在一个固定范围里
     * 
     * @param raw 原始值
     * @param min 最小值
     * @param max 最大值
     * @return 修正后的数值
     */
    public static int clamp(int raw, int min, int max) {
        var res = raw;
        if (res < min)
            res = min;
        else if (res > max)
            res = max;
        return res;
    }

    /**
     * 限制数字在一个固定范围里
     * 
     * @param raw 原始值
     * @param min 最小值
     * @param max 最大值
     * @return 修正后的数值
     */
    public static long clamp(long raw, long min, long max) {
        var res = raw;
        if (res < min)
            res = min;
        else if (res > max)
            res = max;
        return res;
    }

    /**
     * 限制数字在一个固定范围里
     * 
     * @param raw 原始值
     * @param min 最小值
     * @param max 最大值
     * @return 修正后的数值
     */
    public static float clamp(float raw, float min, float max) {
        var res = raw;
        if (res < min)
            res = min;
        else if (res > max)
            res = max;
        return res;
    }

    /**
     * 限制数字在一个固定范围里
     * 
     * @param raw 原始值
     * @param min 最小值
     * @param max 最大值
     * @return 修正后的数值
     */
    public static double clamp(double raw, double min, double max) {
        var res = raw;
        if (res < min)
            res = min;
        else if (res > max)
            res = max;
        return res;
    }

    /**
     * 打印字符串到控制台
     * 
     * @param msg 要打印的内容
     */
    public static void log(String msg) {
        if (msg != null)
            Bukkit.getConsoleSender().sendMessage(msg);
    }

    /**
     * 打印字符串到控制台
     * 
     * @param msg 要打印的内容
     */
    public static void log(String... msg) {
        if (msg != null)
            Bukkit.getConsoleSender().sendMessage(msg);
    }

    /**
     * DeBug模式下打印字符串到控制台
     * 
     * @param msg 要打印的内容
     */
    public static void debug(String msg) {
        if (Config.debug && msg != null)
            Bukkit.getConsoleSender().sendMessage(msg);
    }

    /**
     * DeBug模式下打印字符串到控制台
     * 
     * @param msg 要打印的内容
     */
    public static void debug(String... msg) {
        if (Config.debug && msg != null)
            Bukkit.getConsoleSender().sendMessage(msg);
    }

}
