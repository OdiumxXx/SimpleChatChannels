package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class deluser implements CommandExecutor {   

  public Loader plugin;
  public deluser(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    Player[] players = Bukkit.getOnlinePlayers();
    String PlayerName;

    if(args.length != 2){
      sender.sendMessage("/deluser <ChannelName> <PlayerName>");
      return true;
    }
    String ChanName = args[0].toLowerCase();
    if (player != null) {
      PlayerName = player.getName().toLowerCase();  
    } else {
      PlayerName = "Console";
    }
    String AddPlayName = plugin.myGetPlayerName(args[1]).toLowerCase();
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    if (player != null && !ChowList.contains(PlayerName) && !player.hasPermission("scc.admin") && AddPlayName != args[1]) {
      plugin.NotOwner(sender, ChanName);
      return true;
    }
    boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
    if(ChanTemp == false) {
      plugin.NotExist(sender, ChanName);
      return true;
    } else {
      List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".AccList"); // get the player list
      if (!ChList.contains(AddPlayName)) {
        sender.sendMessage(plugin.DARK_RED+"[SCC] "+plugin.GOLD + AddPlayName + plugin.DARK_RED+ " is not in " + plugin.GOLD+  ChanName);
        return true;
      } else {
        ChList.remove(AddPlayName);  // remove the player from the access list
        plugin.getStorageConfig().set(ChanName+".AccList", ChList); // set the new access list
        plugin.saveStorageConfig();
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD + AddPlayName + plugin.DARK_GREEN + " removed from " + plugin.GOLD + ChanName + "'s" + plugin.DARK_GREEN + " access list");
        Player target = plugin.getServer().getPlayer(AddPlayName);
        if(target != null) {
          target.sendMessage(plugin.DARK_GREEN+"[SCC] "+"You have been removed from " + plugin.GOLD + ChanName + "'s" + plugin.DARK_GREEN + " access list"); 
          }
        List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".AccList");
        for(Player op: players){
          if(ChanList.contains(op.getName())) {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD+ AddPlayName +plugin.DARK_GREEN + " has been removed from" + plugin.GOLD + ChanName + "'s " + plugin.DARK_GREEN + "acces list by " + PlayerName);              
          }
        }
        return true;
      }
    }
  }
}