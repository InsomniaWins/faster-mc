package wins.insomnia.fastermc.eventlisteners;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import wins.insomnia.fastermc.eventlisteners.compass.CompassEvents;

public class PlayerEvents implements Listener {




    private void onPlayerRightClickAir(PlayerInteractEvent event) {

        if (event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
            if (event.getPlayer().getCooldown(Material.COMPASS) == 0) {
                CompassEvents.onCompassRightClicked(event);
            }
        }
    }

    private void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
            if (event.getPlayer().getCooldown(Material.COMPASS) == 0) {
                CompassEvents.onCompassRightClicked(event);
            }
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            onPlayerRightClickAir(event);
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            onPlayerRightClickBlock(event);
        }




    }


}
