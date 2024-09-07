package net.meano.loginlocationfix.listener;

import net.meano.loginlocationfix.modules.LocationProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location joinLoc = player.getLocation();

        LocationProcess.fixStuckInPortal(player, joinLoc);
        LocationProcess.fixStuckUnderground(player, joinLoc);
        LocationProcess.fixOnAir(player, joinLoc); // Dreeam TODO - need to consider if player on the mid air, and the floor is not solid block
    }
}
