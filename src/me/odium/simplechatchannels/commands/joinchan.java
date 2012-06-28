package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
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
    Player[] players = Bukkit.getOnlinePlayers();



    if (player == null) {
      sender.sendMessage("This command can only be run by a player");
      return true;
    }

    if (args.length != 1) {
      sender.sendMessage("/joinchan <channel>");
      return true;
    }


    String ChanName = args[0].toLowerCase();
    String PlayerName = player.getName().toLowerCase();
    boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
    if(ChanTemp == false) {
      sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"Channel " + plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
      return true;
    } 
    if(plugin.getStorageConfig().getBoolean(ChanName+".Locked") == true) {
      if(plugin.getStorageConfig().getStringList(ChanName+".AccList.").contains(PlayerName.toLowerCase())) {
        List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
        List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
        if (ChList.contains(PlayerName)) {
          player.sendMessage(plugin.DARK_GREEN+"[SCC] "+ChatColor.GOLD+plugin.DARK_GREEN + "Already in "+ChatColor.GREEN+ChanName);
          //            togglePluginState(player, args[0]);
        } else {
          ChList.add(PlayerName);  // add the player to the list
          plugin.getStorageConfig().set(ChanName+".list", ChList); // set the new list
          InChatList.add(PlayerName);  // add the player to the list
          plugin.getStorageConfig().set("InChatList", InChatList); // set the new list
          plugin.saveStorageConfig();
          plugin.ChannelThing.put(player, ChanName);
          plugin.pluginEnabled.put(player, true);
          List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
          for(Player op: players){
            if(ChanList.contains(op.getName().toLowerCase())) {
              op.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + PlayerName + ChatColor.DARK_GREEN+" joined "+ ChanName);
            }
          }

        }
      } else {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED + "You must be added to this channel's access list");
        return true;
      }
    } else {
      List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
      List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
      if (ChList.contains(PlayerName)) {
        player.sendMessage(plugin.DARK_GREEN+"[SCC] "+ChatColor.GOLD+plugin.DARK_GREEN + "Already in "+ChatColor.GREEN+ChanName);
      } else {
        ChList.add(PlayerName);  // add the player to the list
        plugin.getStorageConfig().set(ChanName+".list", ChList); // set the new list
        InChatList.add(PlayerName);  // add the player to the list
        plugin.getStorageConfig().set("InChatList", InChatList); // set the new list
        plugin.saveStorageConfig();
        plugin.ChannelThing.put(player, ChanName);
        plugin.pluginEnabled.put(player, true);
        List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
        for(Player op: players){
          if(ChanList.contains(op.getName().toLowerCase())) {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + PlayerName + ChatColor.DARK_GREEN+" joined "+ ChanName);
          }
        }
      }
    }

    return true;
  }
}