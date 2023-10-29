package net.meano.loginlocationfix;

import org.bukkit.plugin.java.JavaPlugin;

public final class LoginLocationFix extends JavaPlugin {

    public static LoginLocationFix plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new LoginLocationFixListeners(), this);
        getLogger().info("LoginLocationFix 0.12,by Meano & Dreeam, Loaded.");
    }

    @Override
    public void onDisable() {
    }

}
