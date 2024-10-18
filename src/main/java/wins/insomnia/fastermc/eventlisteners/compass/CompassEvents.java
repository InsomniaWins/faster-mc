package wins.insomnia.fastermc.eventlisteners.compass;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.util.StructureSearchResult;
import wins.insomnia.fastermc.FasterMC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompassEvents {

	private static final HashMap<Material, ItemStructureMapEntry> ITEM_STRUCTURE_MAP = new HashMap<>();
	static {

		ITEM_STRUCTURE_MAP.put(Material.NETHER_BRICKS, new ItemStructureMapEntry(
				new World.Environment[] {World.Environment.NETHER},
				(player) -> Structure.FORTRESS,
				"compass_can_locate_nether_fortresses"
		));

		ITEM_STRUCTURE_MAP.put(Material.GOLD_BLOCK, new ItemStructureMapEntry(
				new World.Environment[] {World.Environment.NETHER},
				(player) -> Structure.BASTION_REMNANT,
				"compass_can_locate_bastion_remnants"
		));

		ITEM_STRUCTURE_MAP.put(Material.COAL_BLOCK, new ItemStructureMapEntry(
				new World.Environment[] {World.Environment.NORMAL},
				(player) -> Structure.MINESHAFT,
				"compass_can_locate_mineshafts"
		));

		ITEM_STRUCTURE_MAP.put(Material.DIAMOND, new ItemStructureMapEntry(
				new World.Environment[] {World.Environment.NORMAL},
				(player) -> Structure.BURIED_TREASURE,
				"compass_can_locate_buried_treasure"
		));

		ITEM_STRUCTURE_MAP.put(Material.EMERALD, new ItemStructureMapEntry(
				new World.Environment[] {World.Environment.NORMAL},
				(player) -> {
					// cant simply locate village of any type since spigot/paper is dumb
					// so detect a village of the biome type the player is in

					Biome biome = player.getWorld().getBiome(player.getLocation());

					List<Biome> desertBiomes = new ArrayList<>();
					desertBiomes.add(Biome.DESERT);

					List<Biome> plainsBiomes = new ArrayList<>();
					plainsBiomes.add(Biome.PLAINS);
					plainsBiomes.add(Biome.SUNFLOWER_PLAINS);
					plainsBiomes.add(Biome.MEADOW);

					List<Biome> savannaBiomes = new ArrayList<>();
					savannaBiomes.add(Biome.SAVANNA);

					List<Biome> snowyBiomes = new ArrayList<>();
					snowyBiomes.add(Biome.SNOWY_PLAINS);

					List<Biome> taigaBiomes = new ArrayList<>();
					taigaBiomes.add(Biome.TAIGA);
					taigaBiomes.add(Biome.SNOWY_TAIGA);

					if (desertBiomes.contains(biome)) {
						return Structure.VILLAGE_DESERT;
					} else if (plainsBiomes.contains(biome)) {
						return Structure.VILLAGE_PLAINS;
					} else if (savannaBiomes.contains(biome)) {
						return Structure.VILLAGE_SAVANNA;
					} else if (snowyBiomes.contains(biome)) {
						return Structure.VILLAGE_SNOWY;
					} else if (taigaBiomes.contains(biome)) {
						return Structure.VILLAGE_TAIGA;
					} else {
						return Structure.VILLAGE_PLAINS;
					}
				},
				"compass_can_locate_villages"
		));
	}



	private static TextComponent invalidDimensionMessage(String structureName, World.Environment[] correctDimensions) {

		StringBuilder messageString = new StringBuilder("§cYou cannot search for [" + structureName + "] in your current dimension!\nYou must search in [");
		for (int i = 0; i < correctDimensions.length; i++) {
			World.Environment correctDimension = correctDimensions[i];
			messageString.append(correctDimension.name());

			if (i < correctDimensions.length - 1) {
				messageString.append(", ");
			}
		}
		messageString.append("]!");

		return Component.text(messageString.toString());
	}

	public static List<Component> getCompassLore(Player player) {
		ArrayList<Component> loreList = new ArrayList<>();
		loreList.add(Component.text("Valid off-hand items:"));

		int i = 1;
		for (Map.Entry<Material, ItemStructureMapEntry> entry : ITEM_STRUCTURE_MAP.entrySet()) {

			Material material = entry.getKey();
			ItemStructureMapEntry mapEntry = entry.getValue();

			if (FasterMC.getInstance().getConfig().getBoolean(mapEntry.configString)) {

				String itemName = material.getKey().asMinimalString();
				String structureName = mapEntry.structureProvider.getStructure(player).key().asMinimalString();
				String colorString = i % 2 == 0 ? "§6" : "§e";
				String appendString = colorString + i + ") " + itemName + " → " + structureName;
				loreList.add(Component.text(appendString));

				i += 1;
			}

		}

		return loreList;
	}

	public static void onCompassRightClicked(PlayerInteractEvent event) {


		// check config for compass functionality
		if (!FasterMC.getInstance().getConfig().getBoolean("compass_can_locate_structures")) {
			return;
		}


		final int COMPASS_COOLDOWN = 100;
		final int SEARCH_RADIUS = 500;

		Player player = event.getPlayer();

		player.setCooldown(Material.COMPASS, COMPASS_COOLDOWN);

		if (player.getInventory().getItemInOffHand().isEmpty()) {
			ItemStack itemStack = player.getInventory().getItemInMainHand();
			CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
			compassMeta.lore(CompassEvents.getCompassLore(player));
			itemStack.setItemMeta(compassMeta);
			return;
		}

		// if invalid offhand item type, tell player valid types
		Material itemType = player.getInventory().getItemInOffHand().getType();
		if (!ITEM_STRUCTURE_MAP.containsKey(itemType)) {
			player.sendMessage(Component.text("You need to put an item in your off-hand to locate a structure with the compass. You can find a list by highlighting the compass with your mouse."));
			ItemStack itemStack = player.getInventory().getItemInMainHand();
			CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
			compassMeta.lore(CompassEvents.getCompassLore(player));
			itemStack.setItemMeta(compassMeta);
			return;
		}


		ItemStructureMapEntry itemStructureMapEntry = ITEM_STRUCTURE_MAP.get(itemType);
		Structure structure = itemStructureMapEntry.structureProvider.getStructure(player);
		String structureName = structure.key().asMinimalString();


		// check config for specific structure
		if (itemStructureMapEntry.configString == null || itemStructureMapEntry.configString.isEmpty()) {
			return;
		}
		if (!FasterMC.getInstance().getConfig().getBoolean(itemStructureMapEntry.configString)) {
			return;
		}


		// check dimension
		boolean isCorrectEnvironment = false;
		for (World.Environment correctEnvironment : itemStructureMapEntry.correctEnvironments) {
			if (correctEnvironment == player.getWorld().getEnvironment()) {
				isCorrectEnvironment = true;
				break;
			}
		}
		if (!isCorrectEnvironment) {
			TextComponent message = invalidDimensionMessage(structureName, itemStructureMapEntry.correctEnvironments);
			player.sendMessage(message);
			return;
		}



		StructureSearchResult searchResult = player.getWorld().locateNearestStructure(player.getLocation(), structure, SEARCH_RADIUS, false);
		double resultDistance;
		boolean foundStructure = false;

		if (searchResult != null) {
			resultDistance = searchResult.getLocation().distance(player.getLocation());

			if (resultDistance <= SEARCH_RADIUS) {

				foundStructure = true;

				// send result message to player
				Component resultMessage = Component.text("Found " + structureName + " within " + SEARCH_RADIUS + " blocks!");
				player.sendMessage(resultMessage);

				// play ding sound effect
				float distancePitch = 1f - (float) (resultDistance / (double) SEARCH_RADIUS);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, distancePitch);

				// make compass point towards structure
				player.setCompassTarget(searchResult.getLocation());

				// handle compass meta
				if (event.getItem() != null) {

					ItemStack itemStack = event.getItem();
					CompassMeta itemMeta = (CompassMeta) itemStack.getItemMeta();


					// compass item name
					itemMeta.displayName(Component.text("§fStructure: " + structureName));


					// make compass work in nether
					itemMeta.setLodestone(searchResult.getLocation());
					itemMeta.setLodestoneTracked(false);


					event.getItem().setItemMeta(itemMeta);
				}

			}
		}


		if (!foundStructure) {

			// send failed message
			player.sendMessage(Component.text("Could not find [" + structureName + "] within " + SEARCH_RADIUS + " blocks."));

		}
	}

}
