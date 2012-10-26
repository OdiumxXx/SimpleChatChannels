package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addowner implements CommandExecutor {   

  public Loader plugin;
  public addowner(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    String PlayerName;

    if(args.length != 2){
      sender.sendMessage("/addowner <ChannelName> <PlayerName>");
      return true;
    }      
    String ChanName = args[0].toLowerCase();
    if (player != null) {
      PlayerName = player.getName().toLowerCase();  
    } else {
      PlayerName = "Console";
    }
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    if (player == null || ChowList.contains(PlayerName) && player.hasPermission("scc.admin")) {
      String AddPlayName = plugin.myGetPlayerName(args[1]).toLowerCase();
      boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        plugin.NotExist(sender, ChanName);
        return true;
      } else {        
        if (ChowList.contains(AddPlayName)) {
          sender.sendMessage(plugin.DARK_RED+"[SCC] "+plugin.GOLD + AddPlayName + plugin.DARK_RED + " already has owner access to " + plugin.GOLD + ChanName);
          return true;
        } else {
          ChowList.add(AddPlayName);  // add the player to the list
          plugin.getStorageConfig().set(ChanName+".owner", ChowList); // set the new list
          sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD+ AddPlayName + plugin.DARK_GREEN + " added to " + plugin.GOLD + ChanName + "'s" + plugin.DARK_GREEN + " owner list");
          Player target = plugin.getServer().getPlayer(AddPlayName);
          if(target != null) { target.sendMessage(plugin.DARK_GREEN+"[SCC] "+"You have been added to " + plugin.GOLD + ChanName + "'s" + plugin.DARK_GREEN + " owner list"); }
          plugin.saveStorageConfig();
          return true;
        }
      }
    } else {
      plugin.NotOwner(sender, ChanName);
      return true;
    }
  }
}