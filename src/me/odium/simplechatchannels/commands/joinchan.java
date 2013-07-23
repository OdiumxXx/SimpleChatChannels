package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class joinchan implements CommandExecutor {   

  public Loader plugin;
  public joinchan(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }   

    if (player == null) {
      sender.sendMessage("This command can only be run by a player");
      return true;
    }
    if (args.length != 1) {
      sender.sendMessage("/joinchan <channel>");
      return true;
    }
    // SET VARIABLES
    String ChanName = args[0].toLowerCase();
    String PlayerName = player.getName().toLowerCase();

    // CHECK CHANNEL EXISTS    
    boolean ChanExist = plugin.getStorageConfig().contains(ChanName);    
    if(ChanExist == false) {
      plugin.NotExist(sender, ChanName);
      return true;
    } 
    
    
    boolean playerInchannel = plugin.InChannel.containsKey(player);
    if (playerInchannel == true) {
      plugin.AlreadyInChannel(player);
      return true;
      
    }

    // IF LOCKED CHANNEL
    if(plugin.getStorageConfig().getBoolean(ChanName+".locked") == true) { // if channel is locked
      if(plugin.getStorageConfig().getStringList(ChanName+".AccList").contains(PlayerName.toLowerCase())) { // if player is in access list
      List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list

        if (ChList.contains(PlayerName)) { // if player is in player list
          player.sendMessage(ChatColor.DARK_GREEN+"[SCC] "+ChatColor.GOLD+ChatColor.DARK_GREEN + "Already in "+ChatColor.GOLD+ChanName); // inform them already in chan
          return true;
        } else {

          plugin.toggleChannel(player, ChanName); // Join Channel
          return true;

        }
      } else { // if player not in access list
        sender.sendMessage(plugin.DARK_RED+"[SCC] You must first be added to "+ChatColor.GOLD+ChanName+"'s"+ChatColor.DARK_RED+" Access List");
        return true;
      }

      // IF NOT LOCKED CHANNEL
    } else {

      List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
      if (ChList.contains(PlayerName)) {
        player.sendMessage(ChatColor.DARK_GREEN+"[SCC] "+ChatColor.GOLD+ChatColor.DARK_GREEN + "Already in "+ChatColor.GOLD+ChanName);
        return true;

      } else {

        plugin.toggleChannel(player, ChanName);
        return true;

      }

    }
  }
}