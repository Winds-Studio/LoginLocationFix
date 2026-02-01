package net.meano.loginlocationfix.config;

import net.meano.loginlocationfix.LoginLocationFix;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static boolean portalFixEnabled, undergroundFixEnabled, midAirFixEnabled;
    public static String portalFixMsg, undergroundFixMsg1, undergroundFixMsg2, midAirFixMsg;

    public static void loadConfig() {
        final FileConfiguration config = LoginLocationFix.instance.getConfig();

        portalFixEnabled = config.getBoolean("portal.Enabled");
        undergroundFixEnabled = config.getBoolean("underground.Enabled");
        midAirFixEnabled = config.getBoolean("midAir.Enabled");

        portalFixMsg = config.getString("portal.Message");
        undergroundFixMsg1 = config.getString("underground.Message1");
        undergroundFixMsg2 = config.getString("underground.Message2");
        midAirFixMsg = config.getString("midAir.Message");
    }
}
