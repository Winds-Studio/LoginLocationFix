package net.meano.loginlocationfix;

import com.tcoded.folialib.FoliaLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.meano.loginlocationfix.listener.LoginLocationFixListeners;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginLocationFix extends JavaPlugin {

    public static LoginLocationFix instance;
    private BukkitAudiences adventure;
    public final FoliaLib foliaLib = new FoliaLib(this);

    @Override
    public void onEnable() {
        instance = this;
        instance.adventure = BukkitAudiences.create(instance);
        new Metrics(instance, 20124);

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new LoginLocationFixListeners(), instance);

        getLogger().info("LoginLocationFix " + instance.getDescription().getVersion() + ", by Meano & Dreeam, Loaded.");
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
