package me.odium.simplechatchannels.listeners;

import java.util.List;
import java.util.logging.Logger;

import me.odium.simplechatchannels.Loader;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {
	public static Loader plugin;
	Logger log = Logger.getLogger("Minecraft");
	
	public PlayerLeave(Loader instance) {
		plugin = instance;
	}
	

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName().toLowerCase();
	  List<String> ChList = plugin.getStorageConfig().getStringList("Channels"); // get the player list
	  List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
	  
		if (plugin.InChannel.containsKey(player)) {
		  log.info("tits");
		  for (String Chan : ChList) {
		    List<String> UserList = plugin.getStorageConfig().getStringList(Chan+".list"); // get the player list
		    if (UserList.contains(playerName)) {
		      UserList.remove(playerName);  // remove the player from the list
	        plugin.getStorageConfig().set(Chan+".list", UserList); // set the new list
		    }
		  }
		  InChatList.remove(playerName);  // remove the player from the list
      plugin.getStorageConfig().set("InChatList", InChatList); // set th
	
		  plugin.InChannel.put(player, false);		  	  
			plugin.InChannel.remove(player);
			plugin.saveStorageConfig();
		}
			
			
			
//		if(plugin.userAttached.containsKey(pltr)) {
//		
//		  for (String Chan : ChList) {
//        List<String> UserList = plugin.getStorageConfig().getStringList(Chan+".list"); // get the player list
//        UserList.remove(pltr.getName());  // remove the player from the list
//        plugin.getStorageConfig().set(Chan+".list", UserList); // set the new list        
//      }
//      InChatList.remove(pltr.getName());  // remove the player from the list
//      plugin.getStorageConfig().set("InChatList", InChatList); // set th
//		  
//		  plugin.InChannel.put(pltr, false);
//		  plugin.userAttached.remove(pltr);		  
//	}
	}
}
