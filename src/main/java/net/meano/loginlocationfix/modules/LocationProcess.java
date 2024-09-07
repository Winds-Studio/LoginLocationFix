package net.meano.loginlocationfix.modules;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.meano.loginlocationfix.LoginLocationFix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Objects;

public class LocationProcess {

    private static final BlockFace[] faces = {BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST};
    private static Material materialPortal = Material.matchMaterial("PORTAL");
    private static boolean isMinHeightAvailable;

    static {
        if (materialPortal == null) {
            materialPortal = Material.matchMaterial("PORTAL_BLOCK");
            if (materialPortal == null) {
                materialPortal = Material.matchMaterial("NETHER_PORTAL");
            }
        }

        try {
            Method getMinHeightMethod = World.class.getMethod("getMinHeight");
            getMinHeightMethod.setAccessible(true);
            isMinHeightAvailable = true;
        } catch (NoSuchMethodException e) {
            isMinHeightAvailable = false;
        }
    }

    private static int getMinHeight(World world) {
        if (isMinHeightAvailable) {
            return world.getMinHeight();
        } else {
            return 0;
        }
    }

    public static void fixStuckInPortal(Player player, Location joinLoc) {
        if (!LoginLocationFix.instance.getConfig().getBoolean("portal.Enabled")) return;

        if (joinLoc.getBlock().getType().equals(materialPortal) || joinLoc.getBlock().getRelative(BlockFace.UP).getType().equals(materialPortal)) {
            Block JoinBlock = joinLoc.getBlock();
            boolean solved = false;

            for (BlockFace face : faces) {
                if (JoinBlock.getRelative(face).getType().equals(Material.AIR) && JoinBlock.getRelative(face).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    LoginLocationFix.instance.foliaLib.getImpl().teleportAsync(player, JoinBlock.getRelative(face).getLocation().add(0.5, 0.1, 0.5));
                    solved = true;
                    break;
                }
            }

            if (!solved) {
                JoinBlock.getRelative(BlockFace.UP).breakNaturally();
                JoinBlock.breakNaturally();
            }

            LoginLocationFix.instance.adventure().player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.instance.getConfig().getString("portal.Message"))));
        }
    }

    public static void fixStuckUnderground(Player player, Location joinLoc) {
        if (!LoginLocationFix.instance.getConfig().getBoolean("underground.Enabled")) return;

        Material upType = joinLoc.getBlock().getRelative(BlockFace.UP).getType();
        World world = player.getWorld();
        int maxHeight = world.getMaxHeight();
        int minHeight = getMinHeight(world);

        if (upType.isOccluding() || upType.equals(Material.LAVA)) {
            for (int i = maxHeight; i >= minHeight; i--) { // Dreeam TODO: Optimize logic? maybe
                joinLoc.setY(i);
                Block joinBlock = joinLoc.getBlock();

                if ((joinBlock.getRelative(BlockFace.DOWN).getType().isBlock())
                        && joinBlock.getType().equals(Material.AIR)
                        && joinBlock.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    if (joinBlock.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)) {
                        joinBlock.getRelative(BlockFace.DOWN).setType(Material.DIRT);
                    }

                    LoginLocationFix.instance.foliaLib.getImpl().teleportAsync(player, joinBlock.getLocation().add(0, 0.1, 0));
                    LoginLocationFix.instance.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.instance.getConfig().getString("underground.Message1")))));
                    break;
                }

                if (i == minHeight) {
                    LoginLocationFix.instance.foliaLib.getImpl().teleportAsync(player, joinBlock.getLocation().add(0, maxHeight + 0.1, 0));
                    LoginLocationFix.instance.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.instance.getConfig().getString("underground.Message2")))));
                }
            }
        }
    }

    public static void fixOnAir(Player player, Location joinLoc) {
        if (!LoginLocationFix.instance.getConfig().getBoolean("midAir.Enabled")) return;

        World world = player.getWorld();
        if (world.getEnvironment() == World.Environment.NORMAL) {
            Location joinBlockLoc = joinLoc.clone().add(0, -0.03, 0);
            if (!player.isOnGround() && joinBlockLoc.getBlock().isEmpty()) {

                Block highestBlock = world.getHighestBlockAt(joinBlockLoc);

                LoginLocationFix.instance.foliaLib.getImpl().teleportAsync(player, new Location(world, joinLoc.getX(), highestBlock.getLocation().getY() + 1.1, joinLoc.getZ()));
                LoginLocationFix.instance.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.instance.getConfig().getString("midAir.Message")))));
            }
        }
    }
}
