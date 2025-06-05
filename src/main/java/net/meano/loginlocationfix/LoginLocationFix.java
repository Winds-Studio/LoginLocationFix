package net.meano.loginlocationfix;

import com.tcoded.folialib.FoliaLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.meano.loginlocationfix.listener.OnJoin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class LoginLocationFix extends JavaPlugin {
    private static final int BSTATS_ID = 20124;
    private static LoginLocationFix instance;
    private static final Logger LOGGER = LogManager.getLogger(LoginLocationFix.class);
    
    private BukkitAudiences adventure;
    private final FoliaLib foliaLib = new FoliaLib(this);

    @Override
    public void onEnable() {
        instance = this;
        initializeAdventure();
        initializeMetrics();
        registerListeners();
        
        saveDefaultConfig();
        logStartupMessage();
    }

    @Override
    public void onDisable() {
        cleanupAdventure();
    }

    public static LoginLocationFix getInstance() {
        return instance;
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Cannot access Adventure when the plugin is disabled!");
        }
        return this.adventure;
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }

    private void initializeAdventure() {
        this.adventure = BukkitAudiences.create(this);
    }

    private void initializeMetrics() {
        new Metrics(this, BSTATS_ID);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
    }

    private void logStartupMessage() {
        LOGGER.info("LoginLocationFix {}, by Meano & Dreeam, has been enabled!", getDescription().getVersion());
    }

    private void cleanupAdventure() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}
