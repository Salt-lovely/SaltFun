package salt.saltfun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commander implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command,
            @NotNull String label, @NotNull String[] args) {
        if ("reload".equalsIgnoreCase(args[0]) && hasPermission(sender, "saltfun.reload")) {
            Utils.log("[Saltfun]更新依赖信息");
            if(sender instanceof Player p) {
                p.sendMessage("Saltfun正在更新依赖信息");
            }
            Config.readConfig(null);
        }
        return true;
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        return sender instanceof ConsoleCommandSender
                || (sender instanceof Player p && (p.isOp() || p.hasPermission(permission)));
    }
}
