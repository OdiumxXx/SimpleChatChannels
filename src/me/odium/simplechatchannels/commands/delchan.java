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
    String PlayerName;

    String ChanName = args[0].toLowerCase();
    if (player != null) {
      PlayerName = player.getName().toLowerCase();  
    } else {
      PlayerName = "Console";
    }
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    List<String> ChannelsList = plugin.getStorageConfig().getStringList("Channels"); // create/get the channel list

    if (player == null || ChowList.contains(PlayerName) || player.hasPermission("scc.admin")) {
      if (!plugin.getStorageConfig().contains(ChanName)) {
        plugin.NotExist(sender, ChanName);
        return true;
      }
      List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
      for(Player op: players){
        if(ChanList.contains(op.getName().toLowerCase())) {
          if(player == null) {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD +ChanName + plugin.DARK_GREEN + " has been deleted by " + plugin.GOLD + "Console" );
          } else {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD +ChanName + plugin.DARK_GREEN + " has been deleted by " + plugin.GOLD + PlayerName );
            plugin.toggleChannel(op, ChanName);
          }
        }
      }
      plugin.getStorageConfig().set(ChanName, null); // delete the channel
      ChannelsList.remove(ChanName);
      plugin.getStorageConfig().set("Channels", ChannelsList); // set the new list
      plugin.saveStorageConfig();      
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD +ChanName + plugin.DARK_GREEN + " has been deleted");
      return true;
    } else {
      plugin.NotOwner(sender, ChanName);
      return true;
    }
  }
}