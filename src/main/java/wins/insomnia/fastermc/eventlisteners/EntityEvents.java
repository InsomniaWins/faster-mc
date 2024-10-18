package wins.insomnia.fastermc.eventlisteners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;
import wins.insomnia.fastermc.FasterMC;
import wins.insomnia.fastermc.eventlisteners.compass.CompassEvents;

import java.util.Random;

public class EntityEvents implements Listener {

    private static final Random RANDOM = new Random();
    static {
        RANDOM.setSeed(System.currentTimeMillis());
    }



    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        boolean monstersDropEnderPearls = FasterMC.getInstance().getConfig().getBoolean("monsters_drop_ender_pearls");
        int monsterEnderPearlDropChance = FasterMC.getInstance().getConfig().getInt("monster_ender_pearl_drop_chance");

        if (monstersDropEnderPearls && (RANDOM.nextInt(monsterEnderPearlDropChance) + 1 == 1)) {
            if (entity instanceof Monster) {

                World world = entity.getWorld();
                world.dropItem(entity.getLocation().clone(), new ItemStack(Material.ENDER_PEARL, 1));

            }
        }

    }


	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent event) {


		if (event.getEntity() instanceof Player player) {
			ItemStack itemStack = event.getItem().getItemStack();


			// add lore to compass for structure hints
			if (itemStack.getType() == Material.COMPASS) {
				CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
				compassMeta.lore(CompassEvents.getCompassLore(player));
				itemStack.setItemMeta(compassMeta);
			}
		}

	}

}
