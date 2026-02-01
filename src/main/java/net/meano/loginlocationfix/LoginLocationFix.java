package net.meano.loginlocationfix;

import com.tcoded.folialib.FoliaLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.meano.loginlocationfix.config.Config;
import net.meano.loginlocationfix.listener.OnJoin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginLocationFix extends JavaPlugin {

    public static LoginLocationFix instance;
    public static final Logger LOGGER = LogManager.getLogger(LoginLocationFix.class.getSimpleName());
    private BukkitAudiences adventure;
    public final FoliaLib foliaLib = new FoliaLib(this);

    @Override
    public void onEnable() {
        instance = this;
        instance.adventure = BukkitAudiences.create(instance);
        new Metrics(instance, 20124);

        loadConfig();
        getServer().getPluginManager().registerEvents(new OnJoin(), instance);

        LOGGER.info("LoginLocationFix {}, by Meano & Dreeam, Loaded.", instance.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    private void loadConfig() {
        saveDefaultConfig();
        Config.loadConfig();
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
