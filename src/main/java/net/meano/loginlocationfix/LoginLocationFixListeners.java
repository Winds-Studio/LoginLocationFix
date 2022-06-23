package net.meano.loginlocationfix;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginLocationFixListeners implements Listener {
    LoginLocationFix LLFM;

    LoginLocationFixListeners(LoginLocationFix Plugin) {
        LLFM = Plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player JoinPlayer = event.getPlayer();
        Location JoinLocation = JoinPlayer.getLocation().getBlock().getLocation().add(0.5, 0.1, 0.5);
        JoinPlayer.teleport(JoinLocation);
        Material UpType = JoinLocation.getBlock().getRelative(BlockFace.UP).getType();
        if (LLFM.getConfig().getBoolean("Portal.Enable")) {
            if (JoinLocation.getBlock().getType().equals(Material.NETHER_PORTAL) || JoinLocation.getBlock().getRelative(BlockFace.UP).getType().equals(Material.NETHER_PORTAL)) {
            Block JoinBlock = JoinLocation.getBlock();
            if (JoinBlock.getRelative(BlockFace.WEST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.WEST).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.EAST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.EAST).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.NORTH).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.NORTH).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.SOUTH).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.SOUTH_EAST).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.SOUTH_WEST).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.NORTH_EAST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.NORTH_EAST).getLocation().add(0.5, 0.1, 0.5));
            } else if (JoinBlock.getRelative(BlockFace.NORTH_WEST).getType().equals(Material.AIR) && JoinBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                JoinPlayer.teleport(JoinBlock.getRelative(BlockFace.NORTH_WEST).getLocation().add(0.5, 0.1, 0.5));
            } else {
                JoinBlock.getRelative(BlockFace.UP).breakNaturally();
                JoinBlock.breakNaturally();
            }
            JoinPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', LLFM.getConfig().getString("Portal.Message")));

        }} else if (LLFM.getConfig().getBoolean("Underground.Enable")) {
            if (UpType.isOccluding() || UpType.equals(Material.LAVA)){
            int MaxHeight = JoinPlayer.getWorld().getMaxHeight() - 2;
            for (int i = 60; i <= MaxHeight; i++) {
                JoinLocation.setY(i);
                Block JoinBlock = JoinLocation.getBlock();
                if ((JoinBlock.getRelative(BlockFace.DOWN).getType().isBlock())
                        && JoinBlock.getType().equals(Material.AIR)
                        && JoinBlock.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    if (JoinBlock.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)) {
                        JoinBlock.getRelative(BlockFace.DOWN).setType(Material.DIRT);
                    }
                    JoinPlayer.teleport(JoinBlock.getLocation().add(0.5, 0.1, 0.5));
                    JoinPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', LLFM.getConfig().getString("Underground.Message1")));
                    break;
                }
                if (i == MaxHeight) {
                    JoinPlayer.teleport(JoinBlock.getLocation().add(0.5, 1.1, 0.5));
                    JoinPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', LLFM.getConfig().getString("Underground.Message2")));
                }
            }
        } }
    }
}
