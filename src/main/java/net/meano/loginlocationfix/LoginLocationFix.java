package net.meano.loginlocationfix;

import com.tcoded.folialib.FoliaLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.meano.loginlocationfix.listener.LoginLocationFixListeners;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginLocationFix extends JavaPlugin {

    public static LoginLocationFix plugin;
    private BukkitAudiences adventure;
    public final FoliaLib foliaLib = new FoliaLib(this);

    @Override
    public void onEnable() {
        plugin = this;
        this.adventure = BukkitAudiences.create(this);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new LoginLocationFixListeners(), this);
        getLogger().info("LoginLocationFix 1.1,by Meano & Dreeam, Loaded.");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
