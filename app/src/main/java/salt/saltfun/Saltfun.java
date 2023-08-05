/*
 * @Author: Salt
 * @Description: 插件入口文件
 */
package salt.saltfun;

import org.bukkit.plugin.java.JavaPlugin;

import salt.saltfun.LeavesDecay.LeavesDecay;

public class Saltfun extends JavaPlugin {

    private static Saltfun instance;

    private LeavesDecay leavesDecay = new LeavesDecay();

    /** 获取实例 */
    public static Saltfun getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // 注册指令
        var cmd = new Commander();
        getCommand("saltfun").setExecutor(cmd);
        getCommand("sf").setExecutor(cmd);

        Config.readConfig(this);
        leavesDecay.onEnable(instance);
        Utils.log("Saltfun 插件启动成功");
    }

    @Override
    public void onDisable() {
        leavesDecay.onDisable(instance);
        Utils.log("Saltfun 插件卸载成功");
    }
}
