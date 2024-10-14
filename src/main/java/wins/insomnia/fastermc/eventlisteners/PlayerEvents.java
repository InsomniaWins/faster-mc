package wins.insomnia.fastermc.eventlisteners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.ArrayList;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {

            StructureSearchResult result = null;
            String structureName = "";

            switch (player.getInventory().getItemInOffHand().getType()) {


                // nether fortress
                case Material.NETHER_BRICKS -> {
                    result = player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.FORTRESS, 250, false);
                    structureName = "a nether fortress";
                }


                // bastion
                case Material.GOLD_BLOCK -> {
                    result = player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.BASTION_REMNANT, 250, false);
                    structureName = "a bastion remnant";
                }


                // village
                case Material.EMERALD -> {

                    ArrayList<StructureSearchResult> villageResults = new ArrayList<>();

                    villageResults.add(player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.VILLAGE_DESERT, 250, false));
                    villageResults.add(player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.VILLAGE_PLAINS, 250, false));
                    villageResults.add(player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.VILLAGE_SAVANNA, 250, false));
                    villageResults.add(player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.VILLAGE_SNOWY, 250, false));
                    villageResults.add(player.getWorld().locateNearestStructure(player.getLocation().clone(), Structure.VILLAGE_TAIGA, 250, false));



                    for (StructureSearchResult villageResult : villageResults) {

                        double resultDistance = (result != null) ? result.getLocation().distance(player.getLocation()) : -1.0;
                        double villageDistance = villageResult.getLocation().distance(player.getLocation());

                        if (result == null || resultDistance > villageDistance) {

                            result = villageResult;

                        }

                    }


                    structureName = "a village";
                }


                // no proper item found
                default -> {}
            }

            if (result != null) {

                Component resultMessage = Component.text("Found " + structureName + " within 250 blocks!");
                player.sendMessage(resultMessage);

                double distance = result.getLocation().distance(player.getLocation());
                float distancePitch = 1f - (float) (distance / 250.0);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, distancePitch);

            } else if (!structureName.isEmpty()) {

                player.sendMessage(Component.text("Could not find " + structureName + " within 250 blocks."));

            }

        }

    }

}
