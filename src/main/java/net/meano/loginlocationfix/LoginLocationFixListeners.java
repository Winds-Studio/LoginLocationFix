package net.meano.loginlocationfix;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginLocationFixListeners implements Listener {
    LoginLocationFix plugin;
    BlockFace[] faces = {BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST};
    private static Material materialPortal = Material.matchMaterial("PORTAL");
    LoginLocationFixListeners(LoginLocationFix Plugin) {
        plugin = Plugin;
    }
    static {
        if (materialPortal == null) {
            materialPortal = Material.matchMaterial("PORTAL_BLOCK");
            if (materialPortal == null) {
                materialPortal = Material.matchMaterial("NETHER_PORTAL");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location JoinLocation = player.getLocation().getBlock().getLocation().add(0.5, 0.1, 0.5);
        if (plugin.getConfig().getBoolean("portal.Enabled")) {
            if (!JoinLocation.getBlock().getType().equals(materialPortal) && !JoinLocation.getBlock().getRelative(BlockFace.UP).getType().equals(materialPortal)) {
                return;
            }
            Block JoinBlock = JoinLocation.getBlock();
            boolean solved = false;
            for (BlockFace face : faces) {
                if (JoinBlock.getRelative(face).getType().equals(Material.AIR) && JoinBlock.getRelative(face).getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    player.teleport(JoinBlock.getRelative(face).getLocation().add(0.5, 0.1, 0.5));
                    solved = true;
                    break;
                }
            }
            if (!solved) {
                JoinBlock.getRelative(BlockFace.UP).breakNaturally();
                JoinBlock.breakNaturally();
            }
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().getString("portal.Message")));
        } else if (plugin.getConfig().getBoolean("underground.Enabled")) {
            Material UpType = JoinLocation.getBlock().getRelative(BlockFace.UP).getType();
            World world = player.getWorld();
            int MaxHeight = world.getMaxHeight();
            int MinHeight = world.getMinHeight();
            if (!UpType.isOccluding() && !UpType.equals(Material.LAVA)) {
                return;
            }
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
                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().getString("underground.Message1")));
                    break;
                }
                if (i == MaxHeight) {
                    player.teleport(JoinBlock.getLocation().add(0.5, 1.1, 0.5));
                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().getString("underground.Message2")));
                }
            }
        } else if (plugin.getConfig().getBoolean("midAir.Enabled")) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("You are in MidAir now."));
//            if ((JoinLocation - MaxHeight) > 2)
        }
    }
}
