package ourstory;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import ourstory.commands.Craft;
import ourstory.events.*;

public class Main extends JavaPlugin {

	public static final String prefix = "OurStory";
	public static final String version = "2.0";

	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("Loading Ourstory...");

		// Registers all events
		Bukkit.getPluginManager().registerEvents(new Player(), this);
		Bukkit.getPluginManager().registerEvents(new Entity(), this);

		var manager = this.getLifecycleManager();
		manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
			final Commands commands = event.registrar();

			commands.register("fun", "some help description string", new Craft());
		});


		// this.getConfig().addDefault("messages.nopermission", "You do not have the permission to perform
		// this command");
		// this.getConfig().options().copyDefaults(true);
		// this.saveConfig();
		// CustomCraft();
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("Disabling Ourstory...");

		// Save inventory, player state, anything the plugin is manipulating
	}
}
