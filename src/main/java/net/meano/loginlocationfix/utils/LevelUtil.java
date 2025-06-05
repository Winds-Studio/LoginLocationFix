package net.meano.loginlocationfix.utils;

import org.bukkit.World;
import java.lang.reflect.Method;

public final class LevelUtil {
    private static final int FALLBACK_MIN_HEIGHT = 0;
    private static final Method MIN_HEIGHT_METHOD;
    
    static {
        Method method;
        try {
            method = World.class.getMethod("getMinHeight");
            method.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            method = null;
        }
        MIN_HEIGHT_METHOD = method;
    }

    private LevelUtil() {
        // Utility class - private constructor
    }

    public static int getMinHeight(World world) {
        if (MIN_HEIGHT_METHOD != null) {
            try {
                return (int) MIN_HEIGHT_METHOD.invoke(world);
            } catch (Exception ignored) {
                // In case of an error, return the default value
            }
        }
        return FALLBACK_MIN_HEIGHT;
    }
}
