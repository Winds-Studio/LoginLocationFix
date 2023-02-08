package net.meano.loginlocationfix;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginLocationFixListeners implements Listener {
    LoginLocationFix plugin;
    LoginLocationFixListeners(LoginLocationFix Plugin) {
        plugin = Plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location JoinLocation = player.getLocation().getBlock().getLocation().add(0.5, 0.1, 0.5);
        Material UpType = JoinLocation.getBlock().getRelative(BlockFace.UP).getType();
        World world = player.getWorld();
        int MaxHeight = world.getMaxHeight();
        int MinHeight = world.getMinHeight();
        if (plugin.getConfig().getBoolean("portal.Enabled")) {
            if (JoinLocation.getBlock().getType().equals(Material.NETHER_PORTAL) || JoinLocation.getBlock().getRelative(BlockFace.UP).getType().equals(Material.NETHER_PORTAL)) {
                Block JoinBlock = JoinLocation.getBlock();
                if (JoinBlock.getRelative(BlockFace.WEST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.WEST).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.EAST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.EAST).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.NORTH).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.NORTH).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.SOUTH).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.SOUTH_EAST).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.SOUTH_WEST).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.NORTH_EAST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.NORTH_EAST).getLocation().add(0.5, 0.1, 0.5));
                } else if (JoinBlock.getRelative(BlockFace.NORTH_WEST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(BlockFace.NORTH_WEST).getLocation().add(0.5, 0.1, 0.5));
                } else {
                    JoinBlock.getRelative(BlockFace.UP).breakNaturally();
                    JoinBlock.breakNaturally();
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("portal.Message")));

            }
        } else if (plugin.getConfig().getBoolean("underground.Enabled")) {
            if (UpType.isOccluding() || UpType.equals(Material.LAVA)) {
                for (int i = MinHeight; i <= MaxHeight; i++) {
                    JoinLocation.setY(i);
                    Block JoinBlock = JoinLocation.getBlock();
                    if ((JoinBlock.getRelative(BlockFace.DOWN).getType().isBlock())
                            && JoinBlock.getType().equals(Material.AIR)
                            && JoinBlock.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                        if (JoinBlock.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)) {
                            JoinBlock.getRelative(BlockFace.DOWN).setType(Material.DIRT);
                        }
                        player.teleport(JoinBlock.getLocation().add(0.5, 0.1, 0.5));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("underground.Message1")));
                        break;
                    }
                    if (i == MaxHeight) {
                        player.teleport(JoinBlock.getLocation().add(0.5, 1.1, 0.5));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("underground.Message2")));
                    }
                }
            }
        } else if (plugin.getConfig().getBoolean("midAir.Enabled")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "You are in MidAir now."));
//            if ((JoinLocation - MaxHeight) > 2)
        }
    }
}
