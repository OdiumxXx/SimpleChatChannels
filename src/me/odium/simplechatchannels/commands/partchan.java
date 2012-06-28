package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class partchan implements CommandExecutor {   

  public Loader plugin;
  public partchan(Loader plugin)  {
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
      sender.sendMessage("/partchan <ChannelName>");
      return true;
    } else {
      String ChanName = args[0].toLowerCase();
      String PlayerName = player.getName().toLowerCase();
      boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.DARK_GREEN + "Channel " + plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
        return true;
      } else {
        List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
        List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
        if (!ChList.contains(PlayerName)) {
          sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD + PlayerName + plugin.DARK_GREEN + " is not in " + plugin.RED + "#" + ChanName);
          return true;
        } else {
          ChList.remove(PlayerName);  // remove the player from the list
          plugin.getStorageConfig().set(ChanName+".list", ChList); // set the new list
          InChatList.remove(PlayerName);  // add the player to the list
          plugin.getStorageConfig().set("InChatList", InChatList); // set the new list
          plugin.saveStorageConfig();
          //          togglePluginState(player, args[0]);
          if(plugin.pluginEnabled.containsKey(player)){
            if(plugin.pluginEnabled.get(player)){
              plugin.pluginEnabled.put(player, false);
              plugin.pluginEnabled.remove(player);
            }
          }
          player.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + PlayerName + ChatColor.DARK_GREEN+" left "+ ChanName);
          List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
          for(Player op: players){
            if(ChanList.contains(op.getName())) {
              op.sendMessage(plugin.DARK_GREEN+"[SCC] "+ ChatColor.GOLD + PlayerName + ChatColor.DARK_GREEN+" left "+ ChanName);
            }
          }
          return true;

        }
      }
    }   
  }
}