package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class delchan implements CommandExecutor {   

  public Loader plugin;
  public delchan(Loader plugin)  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }    
    Player[] players = Bukkit.getOnlinePlayers();

    String ChanName = args[0].toLowerCase();
    String PlayerName = player.getName().toLowerCase();
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    List<String> ChannelsList = plugin.getStorageConfig().getStringList("Channels"); // create/get the channel list
    List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
    if (player == null || ChowList.contains(PlayerName) || player.hasPermission("scc.admin")) {
      if (!plugin.getStorageConfig().contains(ChanName)) {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"Channel " + plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
        return true;
      }
      List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
      for(Player op: players){
        if(ChanList.contains(op.getName())) {
          if(player == null) {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GREEN +ChanName + plugin.DARK_GREEN + " has been deleted by " + plugin.GREEN + "Console" );
          } else {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GREEN +ChanName + plugin.DARK_GREEN + " has been deleted by " + plugin.GREEN + PlayerName );              
          }
        }
      }
      plugin.getStorageConfig().set(ChanName, null); // delete the channel
      ChannelsList.remove(ChanName);
      plugin.getStorageConfig().set("Channels", ChannelsList); // set the new list
      plugin.saveStorageConfig();
      sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.DARK_GREEN + "Channel " + plugin.GREEN + ChanName + plugin.DARK_GREEN + " Deleted");

      for(Player op: players){
        if(ChanList.contains(op.getName())) {
          if(plugin.pluginEnabled.containsKey(op)){
            if(plugin.pluginEnabled.get(op)){
              plugin.pluginEnabled.put(op, false);
              plugin.pluginEnabled.remove(op);
            }
          }                
        }
        if(InChatList.contains(op.getName())) {
          InChatList.remove(op.getName());
          plugin.getStorageConfig().set("InChatList", InChatList); // set the new list
          plugin.saveStorageConfig();
        }
      }
      return true;
    } else {
      sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.DARK_GREEN + "You are not an owner of " + plugin.GREEN + ChanName);
      return true;
    }
  }
}