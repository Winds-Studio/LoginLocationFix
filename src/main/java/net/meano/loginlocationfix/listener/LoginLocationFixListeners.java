package net.meano.loginlocationfix.listener;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.meano.loginlocationfix.LoginLocationFix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;
import java.lang.reflect.Method;

public class LoginLocationFixListeners implements Listener {

    BlockFace[] faces = {BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST};
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

    private int getMinHeight(World world){
        if (isMinHeightAvailable){
            return world.getMinHeight();
        } else {
            return 0;
        }
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location joinLoc = player.getLocation();

        if (LoginLocationFix.plugin.getConfig().getBoolean("portal.Enabled")) {
            if (joinLoc.getBlock().getType().equals(materialPortal) || joinLoc.getBlock().getRelative(BlockFace.UP).getType().equals(materialPortal)) {

                Block JoinBlock = joinLoc.getBlock();
                boolean solved = false;

                for (BlockFace face : faces) {
                    if (JoinBlock.getRelative(face).getType().equals(Material.AIR) && JoinBlock.getRelative(face).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                        LoginLocationFix.plugin.foliaLib.getImpl().teleportAsync(player, JoinBlock.getRelative(face).getLocation().add(0.5, 0.1, 0.5));
                        solved = true;
                        break;
                    }
                }

                if (!solved) {
                    JoinBlock.getRelative(BlockFace.UP).breakNaturally();
                    JoinBlock.breakNaturally();
                }

                LoginLocationFix.plugin.adventure().player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("portal.Message"))));
            }
        }

        if (LoginLocationFix.plugin.getConfig().getBoolean("underground.Enabled")) {
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

                        LoginLocationFix.plugin.foliaLib.getImpl().teleportAsync(player, joinBlock.getLocation().add(0, 0.1, 0));
                        LoginLocationFix.plugin.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("underground.Message1")))));
                        break;
                    }

                    if (i == minHeight) {
                        LoginLocationFix.plugin.foliaLib.getImpl().teleportAsync(player, joinBlock.getLocation().add(0, maxHeight + 0.1, 0));
                        LoginLocationFix.plugin.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("underground.Message2")))));
                    }
                }
            }
        }

        if (LoginLocationFix.plugin.getConfig().getBoolean("midAir.Enabled")) {
            World world = player.getWorld();
            if (world.getEnvironment() == World.Environment.NORMAL) {
                Location joinBlockLoc = joinLoc.clone().add(0, -0.03, 0);
                if (!player.isOnGround() && joinBlockLoc.getBlock().isEmpty()) {

                    Block highestBlock = world.getHighestBlockAt(joinBlockLoc);

                    LoginLocationFix.plugin.foliaLib.getImpl().teleportAsync(player, new Location(world, joinLoc.getX(), highestBlock.getLocation().getY() + 1.1, joinLoc.getZ()));
                    LoginLocationFix.plugin.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("midAir.Message")))));
                }
            }
        }
    }
}
