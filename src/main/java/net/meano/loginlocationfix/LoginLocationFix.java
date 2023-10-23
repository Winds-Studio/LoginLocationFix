package net.meano.loginlocationfix;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginLocationFix extends JavaPlugin {

    @Override
    public void onEnable() {
        //Log开始记录
        saveDefaultConfig();
        getLogger().info("LoginLocationFix 0.12-SNAPSHOT,by Meano & Dreeam, Loaded.");
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new LoginLocationFixListeners(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
