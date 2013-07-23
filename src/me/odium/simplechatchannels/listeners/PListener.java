package me.odium.simplechatchannels.listeners;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import me.odium.simplechatchannels.Loader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PListener implements Listener {

  Logger log = Logger.getLogger("Minecraft");

  public Loader plugin;
  public PListener(Loader plugin) {    
    this.plugin = plugin;    
    plugin.getServer().getPluginManager().registerEvents(this, plugin);  
  }


  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerQuit(PlayerQuitEvent event){
    Player player = event.getPlayer();
    String playerName = player.getName().toLowerCase();
    List<String> ChList = plugin.getStorageConfig().getStringList("Channels"); // get the player list
    List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list

    if (plugin.InChannel.containsKey(player)) {
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
  }


  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerChat(AsyncPlayerChatEvent chat) {
    Player player = chat.getPlayer();
    String message = chat.getMessage();

    if (plugin.getConfig().getBoolean("SilenceGeneralChat") == true) { // if silence general chat is enabled
      if (!plugin.InChannel.containsKey(player)) { // if player is not in a channel
        Player[] players = Bukkit.getOnlinePlayers(); // get a list of all players online
        chat.getRecipients().clear(); // clear chat recipients
        Set<Player> chatrecipients = chat.getRecipients(); // get empty recipient list
        for (Player cake : players) { // for all players
          if (!plugin.InChannel.containsKey(cake)) { // if player is not in channel
            chatrecipients.add(cake); // add them as a recipient
          }
        }
      }
    }


    if(plugin.InChannel.containsKey(player)){ // if key says player is in a channel
      String Chan = plugin.ChannelMap.get(player); // get the channel
      log.info("[" + Chan + " / " + player.getDisplayName() + "]" + message); // log the message to console

      Player[] players = Bukkit.getOnlinePlayers(); // get all online players      
      List<String> ChanList = plugin.getStorageConfig().getStringList(Chan+".list"); // get the list of users in channel

      String prefixTemp = plugin.getConfig().getString("ChatPrefix.Prefix").replace("`player", player.getDisplayName()).replace("`channel", Chan);      
      String prefix = plugin.replaceColorMacros(prefixTemp); 

      for(Player op : players){        
        if (plugin.SpyMap.containsKey(op)) { // If player is using spychan
          if (plugin.SpyMap.get(op) == "all" || plugin.SpyMap.get(op).equalsIgnoreCase(Chan)) { // if player is spying on all or specific channel being used
            op.sendMessage(ChatColor.RED+"SPY: "+ChatColor.RESET+prefix +" "+ message); // send them the channel message
          }          
        }


        if(ChanList.contains(op.getName().toLowerCase())) { // if the channel list contains the name of an online player
          op.sendMessage(prefix +" "+ message); // send them the channel message
        }
      }
      chat.setCancelled(true); // cancel the message for everyone else
    }
  }

}

