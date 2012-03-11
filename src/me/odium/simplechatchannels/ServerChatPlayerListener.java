package me.odium.simplechatchannels;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ServerChatPlayerListener implements Listener {
	public static Loader plugin;
	Logger log = Logger.getLogger("Minecraft");
	
	public ServerChatPlayerListener(Loader instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent chat) {
		if(plugin.overRide == 1){
			plugin.overRide = 0;
		}
		else{
		  Player p = chat.getPlayer();
	    Player player = chat.getPlayer();
	    String message = chat.getMessage();
	    if(plugin.pluginEnabled.containsKey(p)){
	      String Chan = plugin.ChannelThing.get(player);
	      Player[] players = Bukkit.getOnlinePlayers();
	      log.info("[" + p.getName() + "]" + message + " --> "+ Chan);
	      for(Player op: players){
	        if(op.hasPermission("OpTalk.chat")) {
	          op.sendMessage(plugin.AQUA + "[" + plugin.GREEN + Chan + plugin.AQUA + "/" + p.getDisplayName() + "]" + ChatColor.GREEN + " " + message);
	        }
	      }
	        chat.setCancelled(true);
		}
		}
	}
	}
