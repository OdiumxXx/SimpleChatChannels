package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class chanlist implements CommandExecutor {   

  public Loader plugin;
  public chanlist(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {

    if(args.length == 0) {
      List<String> ChannelsList = plugin.getStorageConfig().getStringList("Channels"); // create/get the channel list
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"Channel List"+ChatColor.GOLD+" ]");
//      sender.sendMessage("- Name --- Users --- Topic --");
      for (String Chan : ChannelsList) {
        int UserCount = plugin.getStorageConfig().getStringList(Chan+".list").size(); // Count the users in the channel
        String topic = plugin.getStorageConfig().getString(Chan+".topic");
        
        // CHECK TOPIC IS NOT NULL - SHORTEN TOPIC
        if (topic == null) {
          topic = " (No Topic Set) ";
        } else if (topic.length() > 42) {
          topic = topic.substring(0, 42)+"...";
        }
        
        if (plugin.getStorageConfig().getBoolean(Chan+".locked") == true) {
          sender.sendMessage(ChatColor.GOLD + "* " + plugin.WHITE + Chan + ("L")+ ChatColor.GOLD+" ["+ChatColor.WHITE+UserCount+ChatColor.GOLD+"] "+"- "+ChatColor.WHITE+topic);
        } else {
          sender.sendMessage(ChatColor.GOLD + "* " + plugin.WHITE + Chan + ChatColor.GOLD+" - ["+ChatColor.WHITE+UserCount+ChatColor.GOLD+"] - "+ChatColor.WHITE+topic);
        }
      }
      return true;
      // FIXED ONLY TO HERE
    } else if(args.length == 1) {
      String ChanName = args[0].toLowerCase();
      if (!plugin.getStorageConfig().getStringList("Channels").contains(ChanName)) {
        plugin.NotExist(sender, ChanName);
        return true;
      }      
      java.util.List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
      sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " User List");
      for(int i = 0; i < ChList.size(); ++i) {
        String ChPlayers = ChList.get(i);
        sender.sendMessage(ChatColor.GOLD+" - "+plugin.WHITE + ChPlayers);          
      }
      return true;
    } else if(args.length == 2 && args[0].contains("-p")) {
      String playerCheck = plugin.myGetPlayerName(args[1]);
      Player target = Bukkit.getPlayer(playerCheck);
      if (plugin.InChannel.containsKey(target)) {
        String Chan = plugin.ChannelMap.get(target);
        sender.sendMessage(plugin.DARK_GREEN+ "Player "+ ChatColor.GOLD +playerCheck+plugin.DARK_GREEN+" is in "+ChatColor.GOLD+Chan);
        return true;
      } else {
        sender.sendMessage(plugin.DARK_GREEN+ "Player "+ ChatColor.GOLD +playerCheck+plugin.DARK_GREEN+" is not in a channel");
        return true;
      }
      
      
    } else if(args.length == 2 && args[0].contains("-o")) {
      String ChanName = args[1].toLowerCase();
      if (!plugin.getStorageConfig().contains(ChanName)) {
        plugin.NotExist(sender, ChanName);
        return true;
      }      
      List<String> OwList = plugin.getStorageConfig().getStringList(ChanName+".owner"); // create/get the owner list
      sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " Owner List");
      for(int i = 0; i < OwList.size(); ++i) {
        String ChOwners = OwList.get(i);              
        sender.sendMessage(ChatColor.GOLD+" - "+plugin.WHITE + ChOwners);          
      }
      return true;
    } else if(args.length == 2 && args[0].contains("-a")) {
      String ChanName = args[1].toLowerCase();
      if (!plugin.getStorageConfig().contains(ChanName)) {
        plugin.NotExist(sender, ChanName);
        return true;
      }      
      List<String> AccList = plugin.getStorageConfig().getStringList(ChanName+".AccList"); // create/get the owner list
      sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " Access List");
      for(int i = 0; i < AccList.size(); ++i) {
        String ChAccess = AccList.get(i);              
        sender.sendMessage(ChatColor.GOLD+" - "+plugin.WHITE + ChAccess);          
      }
      return true;
    }       
    return true;   
  }
}