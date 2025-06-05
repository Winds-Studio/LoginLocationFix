package net.meano.loginlocationfix.modules;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.meano.loginlocationfix.LoginLocationFix;
import net.meano.loginlocationfix.utils.LevelUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class LocationProcess {
    private static final BlockFace[] ADJACENT_FACES = {
        BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH,
        BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST
    };
    
    private static final Material PORTAL_MATERIAL = findPortalMaterial();
    private static final double TELEPORT_Y_OFFSET = 0.1;
    private static final double AIR_CHECK_OFFSET = -0.03;

    private static Material findPortalMaterial() {
        Material material = Material.matchMaterial("PORTAL");
        if (material == null) {
            material = Material.matchMaterial("PORTAL_BLOCK");
            if (material == null) {
                material = Material.matchMaterial("NETHER_PORTAL");
            }
        }
        return material;
    }

    private LocationProcess() {} // Utility class

    public static void fixStuckInPortal(Player player, Location joinLoc) {
        LoginLocationFix plugin = LoginLocationFix.instance;
        if (!plugin.getConfig().getBoolean("portal.Enabled")) return;

        Block joinBlock = joinLoc.getBlock();
        if (!isPortalBlock(joinBlock) && !isPortalBlock(joinBlock.getRelative(BlockFace.UP))) {
            return;
        }

        for (BlockFace face : ADJACENT_FACES) {
            Block adjacent = joinBlock.getRelative(face);
            if (isSafeTeleportSpot(adjacent)) {
                plugin.foliaLib.getScheduler().teleportAsync(player, 
                    adjacent.getLocation().add(0.5, TELEPORT_Y_OFFSET, 0.5));
                sendMessage(player, "portal.Message");
                return;
            }
        }

        // No safe spot found, break portal blocks
        joinBlock.getRelative(BlockFace.UP).breakNaturally();
        joinBlock.breakNaturally();
        sendMessage(player, "portal.Message");
    }

    public static void fixStuckUnderground(Player player, Location joinLoc) {
        LoginLocationFix plugin = LoginLocationFix.instance;
        if (!plugin.getConfig().getBoolean("underground.Enabled")) return;

        Material upType = joinLoc.getBlock().getRelative(BlockFace.UP).getType();
        if (!upType.isOccluding() && upType != Material.LAVA) return;

        World world = player.getWorld();
        Location tempLoc = joinLoc.clone();
        int maxHeight = world.getMaxHeight();
        int minHeight = LevelUtil.getMinHeight(world);

        for (int y = maxHeight; y >= minHeight; y--) {
            tempLoc.setY(y);
            Block block = tempLoc.getBlock();
            Block blockBelow = block.getRelative(BlockFace.DOWN);

            if (isSafeUndergroundSpot(block, blockBelow)) {
                if (blockBelow.getType() == Material.LAVA) {
                    blockBelow.setType(Material.DIRT);
                }
                plugin.foliaLib.getScheduler().teleportAsync(player, 
                    block.getLocation().add(0, TELEPORT_Y_OFFSET, 0));
                sendMessage(player, "underground.Message1");
                return;
            }

            if (y == minHeight) {
                plugin.foliaLib.getScheduler().teleportAsync(player,
                    tempLoc.setY(maxHeight + TELEPORT_Y_OFFSET));
                sendMessage(player, "underground.Message2");
            }
        }
    }

    public static void fixOnAir(Player player, Location joinLoc) {
        LoginLocationFix plugin = LoginLocationFix.instance;
        if (!plugin.getConfig().getBoolean("midAir.Enabled") 
            || shouldSkipAirCheck(player) 
            || player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            return;
        }

        Location joinBlockLoc = joinLoc.clone().add(0, AIR_CHECK_OFFSET, 0);
        if (player.isOnGround() || !joinBlockLoc.getBlock().isEmpty()) {
            return;
        }

        Block highestBlock = joinLoc.getWorld().getHighestBlockAt(joinBlockLoc);
        plugin.foliaLib.getScheduler().teleportAsync(player,
            new Location(joinLoc.getWorld(), joinLoc.getX(), 
                highestBlock.getY() + 1.1, joinLoc.getZ()));
        sendMessage(player, "midAir.Message");
    }

    private static boolean isPortalBlock(Block block) {
        return block.getType() == PORTAL_MATERIAL;
    }

    private static boolean isSafeTeleportSpot(Block block) {
        return block.getType() == Material.AIR 
            && block.getRelative(BlockFace.UP).getType() == Material.AIR;
    }

    private static boolean isSafeUndergroundSpot(Block block, Block blockBelow) {
        return blockBelow.getType().isBlock()
            && block.getType() == Material.AIR
            && block.getRelative(BlockFace.UP).getType() == Material.AIR;
    }

    private static boolean shouldSkipAirCheck(Player player) {
        return player.isDead()
            || player.getGameMode() == GameMode.CREATIVE 
            || player.getGameMode() == GameMode.SPECTATOR
            || player.isFlying() 
            || player.isGliding()
            || player.isInsideVehicle();
    }

    private static void sendMessage(Player player, String configPath) {
        String message = LoginLocationFix.instance.getConfig().getString(configPath);
        if (message != null) {
            LoginLocationFix.instance.adventure().player(player)
                .sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }
}
