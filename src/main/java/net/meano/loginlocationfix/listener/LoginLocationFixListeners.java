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

public class LoginLocationFixListeners implements Listener {

    BlockFace[] faces = {BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST};
    private static Material materialPortal = Material.matchMaterial("PORTAL");

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
        if (LoginLocationFix.plugin.getConfig().getBoolean("portal.enabled")) {
            if (!JoinLocation.getBlock().getType().equals(materialPortal) && !JoinLocation.getBlock().getRelative(BlockFace.UP).getType().equals(materialPortal)) {
                return;
            }
            Block JoinBlock = JoinLocation.getBlock();
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
            LoginLocationFix.plugin.adventure().player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("portal.message"))));
        } else if (LoginLocationFix.plugin.getConfig().getBoolean("underground.enabled")) {
            Material UpType = JoinLocation.getBlock().getRelative(BlockFace.UP).getType();
            if (!UpType.isOccluding() && !UpType.equals(Material.LAVA)) return;
            World world = player.getWorld();
            int MaxHeight = world.getMaxHeight();
            int MinHeight = world.getMinHeight();
            if (!UpType.isOccluding() && !UpType.equals(Material.LAVA)) {
                return;
            }
            for (int i = MinHeight; i <= MaxHeight; i++) { // Dreeam TODO: need from MaxHeight to MinHeight, to prevent teleport to cave underground
                JoinLocation.setY(i);
                Block JoinBlock = JoinLocation.getBlock();
                if ((JoinBlock.getRelative(BlockFace.DOWN).getType().isBlock())
                        && JoinBlock.getType().equals(Material.AIR)
                        && JoinBlock.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    if (JoinBlock.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)) {
                        JoinBlock.getRelative(BlockFace.DOWN).setType(Material.DIRT);
                    }
                    LoginLocationFix.plugin.foliaLib.getImpl().teleportAsync(player, JoinBlock.getLocation().add(0.5, 0.1, 0.5));
                    LoginLocationFix.plugin.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("underground.message1")))));
                    break;
                }
                if (i == MaxHeight) {
                    LoginLocationFix.plugin.foliaLib.getImpl().teleportAsync(player, JoinBlock.getLocation().add(0.5, 1.1, 0.5));
                    LoginLocationFix.plugin.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(LoginLocationFix.plugin.getConfig().getString("underground.message2")))));
                }
            }
        } else if (LoginLocationFix.plugin.getConfig().getBoolean("midAir.enabled")) {
            if (!player.isOnGround()) { // TODO
                LoginLocationFix.plugin.adventure().player(player).sendMessage((LegacyComponentSerializer.legacyAmpersand().deserialize("You are in MidAir now.")));
            }
        }
    }
}
