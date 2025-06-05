package net.meano.loginlocationfix.listener;

import net.meano.loginlocationfix.modules.LocationProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class OnJoin implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Location joinLoc = player.getLocation().clone(); // Clone the location for security
        
        // Consistent handling of possible problem situations
        LocationProcess.fixStuckInPortal(player, joinLoc);
        if (player.isValid()) { // Checking that the player is still in the game
            LocationProcess.fixStuckUnderground(player, joinLoc);
        }
        if (player.isValid()) {
            LocationProcess.fixOnAir(player, joinLoc);
        }
    }
}
