package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class adduser implements CommandExecutor {   

  public Loader plugin;
  public adduser(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    Player[] players = Bukkit.getOnlinePlayers();

    if(args.length != 2){
      sender.sendMessage("/adduser <ChannelName> <PlayerName>");
      return true;
    }      
    String ChanName = args[0].toLowerCase();
    String PlayerName = player.getName().toLowerCase();
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    if (!ChowList.contains(PlayerName) && !player.hasPermission("scc.admin")) {
      sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"You are not an owner of " + plugin.RED + ChanName);
      return true;
    }
    String AddPlayName = plugin.myGetPlayerName(args[1]);
    boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
    if(ChanTemp == false) {
      sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+"Channel " + plugin.RED + ChanName + plugin.DARK_GREEN + " does not exist");
      return true;
    } else {
      List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".AccList"); // get the player access list
      if (ChList.contains(AddPlayName)) {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED + AddPlayName + plugin.DARK_GREEN + " already in " + plugin.RED + ChanName + " Access List");
        return true;
      } else {
        ChList.add(AddPlayName);  // add the player to the access list
        plugin.getStorageConfig().set(ChanName+".AccList", ChList); // set the new list
        plugin.saveStorageConfig();
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GREEN + AddPlayName + plugin.DARK_GREEN + " added to " + plugin.GREEN + ChanName + "'s" + plugin.DARK_GREEN + " access list");
        List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
        for(Player op: players){
          if(ChanList.contains(op.getName())) {
            op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GREEN +AddPlayName + plugin.DARK_GREEN + " has been added to" + plugin.GREEN + " #" + ChanName + "'s " + plugin.DARK_GREEN + "Access List by " + PlayerName);              
          }
        }

        return true;
      }
    }
  }
}