package wins.insomnia.fastermc.eventlisteners.compass;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;

public class ItemStructureMapEntry  {

	public World.Environment[] correctEnvironments;
	public IStructureProvider structureProvider;
	public String configString;

	public ItemStructureMapEntry(World.Environment[] correctEnvironments, IStructureProvider structureProvider, String configString) {
		this.correctEnvironments = correctEnvironments;
		this.structureProvider = structureProvider;
		this.configString = configString;
	}

	public interface IStructureProvider {
		Structure getStructure(Player player);
	}
}
