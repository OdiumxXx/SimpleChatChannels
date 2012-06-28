package me.odium.simplechatchannels.commands;

import java.util.List;

import me.odium.simplechatchannels.Loader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class kuser implements CommandExecutor {   

  public Loader plugin;
  public kuser(Loader plugin)  {
    this.plugin = plugin;
  }


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
    Player[] players = Bukkit.getOnlinePlayers();

    if(args.length != 2){
      sender.sendMessage("/kuser <ChannelName> <PlayerName>");
      return true;
    }

    String ChanName = args[0].toLowerCase();
    String PlayerName = player.getName().toLowerCase();
    String AddPlayName = plugin.myGetPlayerName(args[1]).toLowerCase();
    Player target = Bukkit.getPlayer(AddPlayName);
    List<String> ChowList = plugin.getStorageConfig().getStringList(ChanName+".owner");
    if (player == null || ChowList.contains(PlayerName) ) {
      boolean ChanTemp = plugin.getStorageConfig().contains(ChanName);
      if(ChanTemp == false) {
        sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED +  ChanName + plugin.DARK_GREEN + " does not exist");
        return true;
      } else {
        List<String> ChList = plugin.getStorageConfig().getStringList(ChanName+".list"); // get the player list
        List<String> InChatList = plugin.getStorageConfig().getStringList("InChatList"); // get the player list
        if (!ChList.contains(AddPlayName)) {
          sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.RED + AddPlayName + plugin.DARK_GREEN + " is not in " + plugin.RED +  ChanName);
          return true;
        } else {
          ChList.remove(AddPlayName);  // remove the player from the list
          plugin.getStorageConfig().set(ChanName+".list", ChList); // set the new list
          InChatList.remove(AddPlayName);  // add the player to the list
          plugin.getStorageConfig().set("InChatList", InChatList); // set the new list
          plugin.saveStorageConfig();

          sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD + AddPlayName + plugin.DARK_GREEN + " removed from " + plugin.GREEN +  ChanName + "'s" + plugin.DARK_GREEN + " user list");
          if(target != null) {
            target.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GREEN +  plugin.DARK_GREEN + "You have been removed from " + plugin.GREEN +  ChanName + "'s" + plugin.DARK_GREEN + " user list");
            if(plugin.pluginEnabled.containsKey(target)){
              if(plugin.pluginEnabled.get(target)){
                plugin.pluginEnabled.put(target, false);
                plugin.pluginEnabled.remove(target);
              }
            }
          }
          List<String> ChanList = plugin.getStorageConfig().getStringList(ChanName+".list");
          for(Player op: players){
            if(ChanList.contains(op.getName())) {
              op.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.GOLD +  AddPlayName + plugin.DARK_GREEN + " has been removed from" + plugin.GREEN + " #" + ChanName + plugin.DARK_GREEN + " by " + PlayerName);              
            }
          }
          return true;
        }
      }
    } else {
      sender.sendMessage(plugin.DARK_GREEN+"[SCC] "+plugin.DARK_GREEN + "You are not an owner of " + plugin.RED +  ChanName);
      return true;
    }
  }
}