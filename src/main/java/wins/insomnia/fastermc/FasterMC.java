package wins.insomnia.fastermc;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wins.insomnia.fastermc.eventlisteners.EntityEvents;

public final class FasterMC extends JavaPlugin {

    @Override
    public void onEnable() {
        EntityEvents entityEventsListenerInstance = new EntityEvents();
        getServer().getPluginManager().registerEvents(entityEventsListenerInstance, this);
    }

    @Override
    public void onDisable() {
    }
}
