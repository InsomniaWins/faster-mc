package wins.insomnia.fastermc;

import org.bukkit.plugin.java.JavaPlugin;
import wins.insomnia.fastermc.eventlisteners.EntityEvents;
import wins.insomnia.fastermc.eventlisteners.PlayerEvents;

public final class FasterMC extends JavaPlugin {

    private static FasterMC instance = null;


    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();


        EntityEvents entityEventsListenerInstance = new EntityEvents();
        getServer().getPluginManager().registerEvents(entityEventsListenerInstance, this);

        PlayerEvents playerEventsListenerInstance = new PlayerEvents();
        getServer().getPluginManager().registerEvents(playerEventsListenerInstance, this);


    }

    @Override
    public void onDisable() {
    }

    public static FasterMC getInstance() {
        return instance;
    }
}
