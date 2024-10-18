package wins.insomnia.fastermc.eventlisteners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import wins.insomnia.fastermc.eventlisteners.compass.CompassEvents;

public class InventoryEvents implements Listener {

	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {

		if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.COMPASS) {

			if (event.getWhoClicked() instanceof Player player) {
				ItemStack itemStack = event.getCurrentItem();
				CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
				compassMeta.lore(CompassEvents.getCompassLore(player));
				itemStack.setItemMeta(compassMeta);
			}

		}

	}


}
