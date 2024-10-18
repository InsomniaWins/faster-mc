package wins.insomnia.fastermc.eventlisteners.compass;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;

public class ItemStructureMapEntry  {

	public World.Environment[] correctEnvironments;
	public IStructureProvider structureProvider;

	public ItemStructureMapEntry(World.Environment[] correctEnvironments, IStructureProvider structureProvider) {
		this.correctEnvironments = correctEnvironments;
		this.structureProvider = structureProvider;

	}

	public interface IStructureProvider {
		Structure getStructure(Player player);
	}
}
