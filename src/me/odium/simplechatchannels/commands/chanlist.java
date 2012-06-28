package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class chanlist implements CommandExecutor {   

  public Loader plugin;
  public chanlist(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {

    if(args.length == 0) {
      List<String> ChannelsList = plugin.getStorageConfig().getStringList("Channels"); // create/get the channel list
      sender.sendMessage(ChatColor.GOLD + "[ "+ChatColor.WHITE+"Channel List"+ChatColor.GOLD+" ]");
      for(int i = 0; i < ChannelsList.size(); ++i) {
        String ChannelNames = ChannelsList.get(i);
        if (plugin.getStorageConfig().getBoolean(ChannelNames+".locked") == true) {
          sender.sendMessage(ChatColor.GOLD + " - " + plugin.WHITE + ChannelNames + ChatColor.GOLD + " [Locked]");
        } else {
          sender.sendMessage(ChatColor.GOLD + " - " + plugin.WHITE + ChannelNames);
        }
      }
      return true;
    } else if(args.length == 1) {
      String ChanName = args[0].toLowerCase();
      if (!plugin.getStorageConfig().contains(ChanName)) {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
        return true;
      }      
      java.util.List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
      sender.sendMessage(ChatColor.GOLD + "Channel " + ChanName + "'s" + " User List");
      for(int i = 0; i < ChList.size(); ++i) {
        String ChPlayers = ChList.get(i);
        sender.sendMessage(ChatColor.GOLD+" - "+plugin.WHITE + ChPlayers);          
      }
      return true;
    } else if(args.length == 2 && args[0].contains("-o")) {
      String ChanName = args[1].toLowerCase();
      if (!plugin.getStorageConfig().contains(ChanName)) {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
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
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
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