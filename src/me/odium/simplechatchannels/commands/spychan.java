package me.odium.simplechatchannels.commands;

import me.odium.simplechatchannels.Loader;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spychan implements CommandExecutor {   

  public Loader plugin;
  public spychan(Loader plugin)  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }    

    if (args.length == 0) { // if no channel specified
      if (plugin.SpyMap.containsKey(player)) { // if player already spying on channel/s
        plugin.SpyMap.remove(player); // stop spying        
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"No longer spying on: "+ChatColor.GOLD+"All Channels"); // notify the user
        return true;
      } else { // otherwise
        plugin.SpyMap.put(player, "all"); // toggle spying on all channels for player  
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"Spying on: "+ChatColor.GOLD+"All Channels"); // notify the user
        return true;
      }

    } else if (args.length == 1) { // if channel to spy on specified      
      String spyChannel = args[0]; 
      if (plugin.SpyMap.containsKey(player) && plugin.SpyMap.get(player).equalsIgnoreCase(spyChannel)) { // if player already spying on specified channel
        plugin.SpyMap.remove(player); // stop spying
        sender.sendMessage(ChatColor.DARK_GREEN+"[SCC] "+"No Longer spying on channel: "+ChatColor.GOLD+spyChannel); // notify user
        return true;
      }
      boolean channelCheck = plugin.getStorageConfig().contains(spyChannel); // check if channel exists
      if(channelCheck == false) { // if not
        plugin.NotExist(sender, spyChannel); // tell player it doesn't exist and finish
        return true;      
      } else { // otherwise
        plugin.SpyMap.put(player, spyChannel); // toggle spying on specified channel for player        
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"Spying on: "+ChatColor.GOLD+spyChannel); // notify the user


        return true;
      }      
    }     
    return true; 
  }
}