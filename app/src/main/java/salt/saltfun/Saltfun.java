/*
 * @Author: Salt
 * @Description: 插件入口文件
 */
package salt.saltfun;

import org.bukkit.plugin.java.JavaPlugin;

import salt.saltfun.FarmProtect.FarmProtect;
import salt.saltfun.LeavesDecay.LeavesDecay;

public class Saltfun extends JavaPlugin {

    private static Saltfun instance;

    private LeavesDecay leavesDecay = new LeavesDecay();
    private FarmProtect farmProtect = new FarmProtect();

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

        // 读取配置
        Config.readConfig(this);

        // 加载功能
        leavesDecay.onEnable(instance);
        farmProtect.onEnable(instance);

        Utils.log("Saltfun 插件启动成功");
    }

    @Override
    public void onDisable() {
        // 卸载功能
        leavesDecay.onDisable(instance);
        farmProtect.onDisable(instance);

        Utils.log("Saltfun 插件卸载成功");
    }
}
