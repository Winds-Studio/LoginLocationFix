package net.meano.loginlocationfix;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginLocationFix extends JavaPlugin {

    @Override
    public void onEnable() {
        //Log开始记录
        getLogger().info("LoginLocationFix 0.3,by Meano & Dreeam. 正在载入.");
        PluginManager PM = Bukkit.getServer().getPluginManager();
        PM.registerEvents(new LoginLocationFixListeners(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
