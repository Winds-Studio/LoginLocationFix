package net.meano.loginlocationfix.utils;

import org.bukkit.World;

import java.lang.reflect.Method;

public class LevelUtil {

    private static boolean isMinHeightAvailable;

    static {
        try {
            Method getMinHeightMethod = World.class.getMethod("getMinHeight");
            getMinHeightMethod.setAccessible(true);
            isMinHeightAvailable = true;
        } catch (NoSuchMethodException e) {
            isMinHeightAvailable = false;
        }
    }

    public static int getMinHeight(World world) {
        if (isMinHeightAvailable) {
            return world.getMinHeight();
        } else {
            return 0;
        }
    }
}
