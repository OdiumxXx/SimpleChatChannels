package me.odium.simplechatchannels;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave {
	public static Loader plugin;
	Logger log = Logger.getLogger("Minecraft");
	
	public PlayerLeave(Loader instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event){
		Player pltr = event.getPlayer();
		if(plugin.pluginEnabled.containsKey(pltr))
			plugin.pluginEnabled.remove(pltr);
		if(plugin.userAttached.containsKey(pltr))
			plugin.userAttached.remove(pltr);
	}
}
