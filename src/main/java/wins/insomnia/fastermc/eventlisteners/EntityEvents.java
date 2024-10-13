package wins.insomnia.fastermc.eventlisteners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EntityEvents implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        if (entity instanceof Monster) {

            World world = entity.getWorld();
            world.dropItem(entity.getLocation().clone(), new ItemStack(Material.ENDER_PEARL, 1));

        }

    }


}
