package me.odium.simplechatchannels.commands;

import java.util.Arrays;
import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class topic implements CommandExecutor {   

  public Loader plugin;
  public topic(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }

    if (args.length < 2) {
      sender.sendMessage("/topic <channel> <topic> - Set a channel topic");
      return true;
    }

    String playerName = player.getName().toLowerCase();
    String ChannelName = args[0];

    //CHECK CHANNEL EXISTS
    List<String> Channels = plugin.getStorageConfig().getStringList("Channels"); // get the Channels list
    if (!Channels.contains(ChannelName)) {
      plugin.NotExist(sender, ChannelName);
      
      return true;
    }

    if (plugin.ChannelMap.containsKey(player)) {
      String Channel = plugin.ChannelMap.get(player);
      if (!Channel.equalsIgnoreCase(ChannelName)) {
        sender.sendMessage(plugin.DARK_RED+"[SCC] "+ChatColor.DARK_RED+"Must be in "+ChatColor.GOLD+ChannelName+ChatColor.DARK_RED+" to change topic");
        return true;
      }
    } else {
      sender.sendMessage(plugin.DARK_RED+"[SCC] Must be in "+ChatColor.GOLD+ChannelName+ChatColor.DARK_RED+" to change topic");
      return true;
    }

    // CHECK PLAYER HAS OWNER ACCESS TO CHANNEL
    List<String> OwnerList = plugin.getStorageConfig().getStringList(ChannelName+".owner"); // get the player list    
    if (!OwnerList.contains(playerName)) {
      plugin.NotOwner(sender, ChannelName);
      return true;
    }    

    StringBuilder sb = new StringBuilder();
    for (String arg : args)
      sb.append(arg + " ");        
        String[] temp = sb.toString().split(" ");
        String[] temp2 = Arrays.copyOfRange(temp, 1, temp.length);
        sb.delete(0, sb.length());
        for (String details : temp2)
        {
          sb.append(details);
          sb.append(" ");
        }
        String topicArgs = sb.toString();   
        
        plugin.getStorageConfig().set(ChannelName+".topic", topicArgs); // Set the topic
        plugin.saveStorageConfig();

        Player[] players = Bukkit.getOnlinePlayers(); // get all online players      
        for(Player op: players){
          if(plugin.ChannelMap.containsKey(op)) { // if the channel list contains the name of an online player 
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + playerName + ChatColor.DARK_GREEN+" Set topic for "+ChannelName+": "+ChatColor.WHITE+ topicArgs);
          }
        }
        return true;
  }
}